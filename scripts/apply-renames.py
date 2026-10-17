"""Runs the rename table over files already in the project. python -I scripts/apply-renames.py <file> ..."""
import sys, os, io, importlib.util
here = os.path.dirname(os.path.abspath(__file__))
spec = importlib.util.spec_from_file_location('renames', os.path.join(here, 'renames.py'))
r = importlib.util.module_from_spec(spec); spec.loader.exec_module(r)
for f in sys.argv[1:]:
    s = io.open(f, encoding='utf-8', newline='').read()
    t, fired = r.apply(s)
    if t != s:
        io.open(f, 'w', encoding='utf-8', newline='').write(t)
        print(os.path.basename(f), len(fired), 'rules')
