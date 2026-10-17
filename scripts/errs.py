"""Prints build/check.log compile errors compactly, one line each: file line message | code | missing symbol.
  python -I scripts/errs.py [logfile]"""
import re,sys
t=open(sys.argv[1] if len(sys.argv)>1 else 'build/check.log',encoding='utf-8',errors='replace').read().split('\n')
seen=set()
for i,l in enumerate(t):
    m=re.search(r'([\w]+\.java):(\d+): error: (.*)',l)
    if m:
        k=(m.group(1),m.group(2))
        if k in seen: continue
        seen.add(k)
        sym=''
        for q in range(i+2,i+6):
            if q<len(t) and 'symbol:' in t[q]: sym=t[q].strip()[7:];break
        print(m.group(1),m.group(2),m.group(3)[:40],'|',t[i+1].strip()[:80] if i+1<len(t) else '','|',sym[:50])
