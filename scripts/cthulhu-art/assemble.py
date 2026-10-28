import json, os, sys
from PIL import Image

D = sys.argv[1]
OUT = os.path.join(D, 'out')
DEST = sys.argv[2]
GROUPS = {
    'boss': ['idle1', 'hurt', 'mind1', 'eye', 'beam', 'idle3', 'mind3', 'death'],
    'slam': ['slam1_0', 'slam1_1', 'slam1_2', 'slam1_3', 'slam3_0', 'slam3_1', 'slam3_2', 'slam3_3'],
    'strike': ['strike'],
    'grasp': ['grasp_back', 'grasp_front'],
    'cleanse': ['cleanse_back', 'cleanse_front'],
}
info = {}

def load(name):
    m = json.load(open(os.path.join(OUT, name + '.json')))
    x0, y0, x1, y1 = m['region']
    w, h = x1 - x0, y1 - y0
    raw = open(os.path.join(OUT, name + '.rgba'), 'rb').read()
    frames = [Image.frombytes('RGBA', (w, h), raw[f * w * h * 4:(f + 1) * w * h * 4]) for f in range(m['n'])]
    return m, frames

for g, names in GROUPS.items():
    data = {n: load(n) for n in names}
    box = None
    for n, (m, frames) in data.items():
        for f in frames:
            b = f.getbbox()
            if b: box = b if box is None else (min(box[0], b[0]), min(box[1], b[1]), max(box[2], b[2]), max(box[3], b[3]))
    m0 = data[names[0]][0]
    ox, oy = m0['region'][0] + box[0], m0['region'][1] + box[1]
    fw, fh = box[2] - box[0], box[3] - box[1]
    info[g] = {'x': ox, 'y': oy, 'w': fw, 'h': fh, 'anims': {}}
    for n, (m, frames) in data.items():
        crops, seq, seen = [], [], {}
        for f in frames:
            c = f.crop(box)
            k = c.tobytes()
            if k not in seen:
                seen[k] = len(crops); crops.append(c)
            seq.append(seen[k])
        cols = max(1, min(len(crops), 4096 // fw))
        rows = (len(crops) + cols - 1) // cols
        sheet = Image.new('RGBA', (cols * fw, rows * fh))
        for i, c in enumerate(crops):
            sheet.paste(c, ((i % cols) * fw, (i // cols) * fh))
        sheet.save(os.path.join(DEST, n + '.png'), optimize=True)
        info[g]['anims'][n] = {'frames': seq, 'distinct': len(crops), 'shake': m['sh']}
json.dump(info, open(os.path.join(OUT, 'sheets.json'), 'w'))
for g, v in info.items():
    print(g, v['x'], v['y'], v['w'], v['h'], {n: (a['distinct'], len(a['frames'])) for n, a in v['anims'].items()})
