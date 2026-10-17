"""Runs scripts/renames.py over files already in the repo.   python -I scripts/rerename.py file1 file2 ..."""
import sys, io, importlib.util, os
sp = importlib.util.spec_from_file_location('renames', os.path.join(os.path.dirname(os.path.abspath(__file__)), 'renames.py'))
r = importlib.util.module_from_spec(sp); sp.loader.exec_module(r)
for f in sys.argv[1:]:
    s = io.open(f, encoding='utf-8', newline='').read()
    t, fired = r.apply(s)
    if t != s:
        io.open(f, 'w', encoding='utf-8', newline='').write(t); print(f, len(fired))
