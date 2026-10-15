import json, subprocess, os, re, sys, concurrent.futures as cf
IDX = r"C:\Users\onyxr\AppData\Local\Temp\claude\C--Users-onyxr-Documents-onyx-pixel-dungeon\169e99e6-7b80-45ca-8a0a-5f2aa9c3163f\scratchpad\artifact-files\c3216af8-d926-46fd-9c16-d68d9a6e958f\data\index.json"
OUT = r"C:\Users\onyxr\mods-music"
forks = json.load(open(IDX, encoding='utf8'))['forks']
AUD = re.compile(r'\.(ogg|mp3|wav|flac)$', re.I)
KEY = re.compile(r'music|bgm|song|theme|track|ost|soundtrack|bgs', re.I)
def run(a, cwd=None, t=900):
    return subprocess.run(a, cwd=cwd, capture_output=True, text=True, timeout=t, encoding='utf8', errors='replace')
def work(f):
    slug = f['s']; url = f['r']
    d = os.path.join(OUT, slug)
    res = {'slug': slug, 'title': f['t'], 'repo': url, 'files': [], 'err': None}
    try:
        if not os.path.isdir(os.path.join(d, '.git')):
            r = run(['git','-c','core.longpaths=true','clone','--depth','1','--filter=blob:none','--no-checkout','--quiet',url,d])
            if r.returncode: res['err'] = r.stderr[-200:]; return res
        r = run(['git','-C',d,'ls-tree','-r','--name-only','HEAD'])
        names = [n for n in r.stdout.splitlines() if AUD.search(n)]
        # music-ish: audio file whose path says music, or any ogg outside obvious sfx folders
        sel = [n for n in names if KEY.search(n) and not re.search(r'sfx|/sounds?/|snd_', n, re.I)]
        if not sel:
            sel = [n for n in names if n.lower().endswith(('.ogg','.mp3')) and not re.search(r'sfx|/sounds?/|snd_|/res/raw/', n, re.I)]
        res['files'] = sel; res['audio_total'] = len(names)
        if sel:
            run(['git','-C',d,'-c','core.longpaths=true','sparse-checkout','init','--no-cone'])
            open(os.path.join(d,'.git','info','sparse-checkout'),'w',encoding='utf8').write('\n'.join('/'+n for n in sel)+'\n')
            r = run(['git','-C',d,'-c','core.longpaths=true','checkout','--quiet','HEAD'], t=1800)
            if r.returncode: res['err'] = r.stderr[-200:]
    except Exception as e:
        res['err'] = str(e)[:200]
    return res
with cf.ThreadPoolExecutor(6) as ex:
    out = list(ex.map(work, forks))
json.dump(out, open(os.path.join(OUT,'_result.json'),'w'), indent=1)
print('done', sum(1 for o in out if o['files']), 'mods with music;', sum(len(o['files']) for o in out), 'files;', sum(1 for o in out if o['err']), 'errors')
