import os
"""newfiles.py [--renames] [--sub OLD NEW ...]  works on the untracked .java files of the worktree.
--renames runs scripts/apply-renames.py on them. --sub replaces regex OLD with NEW in each (word boundaries are up to you)."""
import subprocess, sys, re, os
os.chdir(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))
out = subprocess.run(['git', 'status', '--short', '--untracked-files=all'], capture_output=True, text=True).stdout
files = [l[3:].strip() for l in out.splitlines() if l.startswith('??') and l.strip().endswith('.java')]
args = sys.argv[1:]
if '--list' in args:
    print('\n'.join(files))
if '--renames' in args:
    r = subprocess.run([sys.executable, '-I', 'scripts/apply-renames.py'] + files, capture_output=True, text=True)
    print((r.stdout + r.stderr)[-500:])
if '--sub' in args:
    i = args.index('--sub') + 1
    pairs = list(zip(args[i::2], args[i+1::2]))
    n = 0
    for f in files:
        t = open(f, encoding='utf-8', newline='').read()
        u = t
        for a, b in pairs:
            u = re.sub(a, b, u)
        if u != t:
            open(f, 'w', encoding='utf-8', newline='').write(u); n += 1
    print('changed', n)
print(len(files), 'new java files')
