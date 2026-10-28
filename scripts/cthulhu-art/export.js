// Runs the Cthulhu animation page's drawing code headless and saves each animation's frames as raw RGBA crops.
// Every frame is drawn twice, on black and on white, and the alpha is worked out from the difference.
const fs=require('fs'),path=require('path');
const D=process.argv[2],RAW=path.join(D,'raw'),OUT=path.join(D,'out');
const img=n=>{const [w,h]=JSON.parse(fs.readFileSync(path.join(RAW,n+'.json')));return {width:w,height:h,data:new Uint8ClampedArray(fs.readFileSync(path.join(RAW,n+'.rgba')))}};
function ctx2d(w,h){const c={width:w,height:h,data:new Uint8ClampedArray(w*h*4)};
  return {imageSmoothingEnabled:false,fillStyle:'#000',
    fillRect(x,y,ww,hh){for(let j=y;j<y+hh;j++)for(let i=x;i<x+ww;i++){const k=(j*w+i)*4;c.data[k]=0;c.data[k+1]=0;c.data[k+2]=0;c.data[k+3]=255}},
    clearRect(){c.data.fill(0)},
    drawImage(im,a,b,cc,d,e,f,g,hh){let sx=0,sy=0,sw=im.width,sh=im.height,dx=a,dy=b,dw=sw,dh=sh;if(cc!==undefined&&e===undefined){dw=cc;dh=d}if(e!==undefined){sx=a;sy=b;sw=cc;sh=d;dx=e;dy=f;dw=g;dh=hh}
      for(let j=0;j<dh;j++)for(let i=0;i<dw;i++){const X=dx+i,Y=dy+j;if(X<0||Y<0||X>=w||Y>=h)continue;const s=((sy+Math.floor(j*sh/dh))*im.width+sx+Math.floor(i*sw/dw))*4,al=im.data[s+3]/255;if(!al)continue;const t=(Y*w+X)*4;
        for(let q=0;q<3;q++)c.data[t+q]=im.data[s+q]*al+c.data[t+q]*(1-al);c.data[t+3]=Math.max(c.data[t+3],im.data[s+3])}},
    getImageData(x,y,ww,hh){const o=new Uint8ClampedArray(ww*hh*4);for(let j=0;j<hh;j++)for(let i=0;i<ww;i++)for(let q=0;q<4;q++)o[(j*ww+i)*4+q]=c.data[((y+j)*w+x+i)*4+q];return {data:o,width:ww,height:hh}},
    createImageData(ww,hh){return {data:new Uint8ClampedArray(ww*hh*4),width:ww,height:hh}},
    putImageData(){}}}
global.document={createElement:()=>{const cv={width:0,height:0,getContext(){if(!cv._c)cv._c=ctx2d(cv.width,cv.height);return cv._c}};return cv}};
global.DCLogic=class{constructor(){}setState(o){Object.assign(this.state,o)}forceUpdate(){}};
global.NOFX=true;global.LAYER='all';
let src=fs.readFileSync(path.join(D,'page.js'),'utf8');
const patch=(a,b,all)=>{if(!src.includes(a))throw new Error('patch missing: '+a);src=all?src.split(a).join(b):src.replace(a,b)};
patch('const rings=RS.map(','const rings=NOFX?[]:RS.map(');
patch('for(let k=0;k<60;k++){const a=hash(k,3)','for(let k=0;k<(NOFX?0:60);k++){const a=hash(k,3)');
patch('for(const ts of RS){const R=(t-ts)*SPD;if(R<=0||R>MAXR)continue;','for(const ts of (NOFX?[]:RS)){const R=(t-ts)*SPD;if(R<=0||R>MAXR)continue;');
patch('if(tint>0.01){const d=this.fb','if(!NOFX&&tint>0.01){const d=this.fb');
patch('const burn=t>FIRE?1:0;','const burn=!NOFX&&t>FIRE?1:0;');
patch('if(t>=FIRE&&flare>0){const p=flare','if(!NOFX&&t>=FIRE&&flare>0){const p=flare');
patch('if(back.length)this.drawTent(back,false);',"if(LAYER!=='front'&&back.length)this.drawTent(back,false);",true);
patch('if(front.length)this.drawTent(front,false);',"if(LAYER!=='back'&&front.length)this.drawTent(front,false);",true);
patch('this.puff(gx-4,gy,0.55',"if(LAYER!=='back')this.puff(gx-4,gy,0.55");
patch('this.puff(gx+4,gy,REL',"if(LAYER!=='back')this.puff(gx+4,gy,REL");
const C=new Function(src+'\nreturn {Component,W,H,DUR,FPS,CX,SPOTS,VIEWS,BEAM_E};')();
const {W,H,DUR,FPS}=C;
const c=new C.Component();c.m=JSON.parse(fs.readFileSync(path.join(__dirname,'..','..','art-for-design','cthulhu-animations','art','arena.json')));
c.build(img('tiles_halls_x4'),img('pillar'),img('pit_alt_52'));
const empty=()=>({d:new Uint8ClampedArray(W*H*4),x0:W,y0:H,x1:-1,y1:-1});
const ORIG={STATIC:c.STATIC,RIM:c.RIM,BODY:c.BODY,units:c.units};
const BG0=new Uint8ClampedArray(W*H*4),BG1=new Uint8ClampedArray(W*H*4);for(let i=0;i<W*H;i++){BG0[i*4+3]=255;BG1[i*4]=BG1[i*4+1]=BG1[i*4+2]=BG1[i*4+3]=255}
function frame(anim,t){c.state.anim=anim;const fr=Math.floor(t*FPS[anim]+1e-6);const r=[];let sh;
  for(const bg of [BG0,BG1]){c.floor=bg;sh=c.draw(fr/FPS[anim],fr)||[0,0];r.push(new Uint8ClampedArray(c.img.data))}
  const [a,b]=r,o=new Uint8ClampedArray(W*H*4);
  for(let i=0;i<W*H*4;i+=4){const df=((b[i]-a[i])+(b[i+1]-a[i+1])+(b[i+2]-a[i+2]))/3,k=1-df/255;if(k<0.02)continue;for(let q=0;q<3;q++)o[i+q]=Math.min(255,a[i+q]/k);o[i+3]=Math.round(k*255)}
  return {o,sh}}
function job(name,anim,dur,region,setup){setup&&setup();const [x0,y0,x1,y1]=region,w=x1-x0,h=y1-y0,n=Math.floor(dur*12+1e-6)+1,buf=Buffer.alloc(w*h*4*n),shs=[];
  for(let f=0;f<n;f++){const {o,sh}=frame(anim,f/12);shs.push(sh);for(let j=0;j<h;j++)for(let i=0;i<w;i++){const s=((y0+j)*W+x0+i)*4,t=((f*h+j)*w+i)*4;for(let q=0;q<4;q++)buf[t+q]=o[s+q]}}
  fs.writeFileSync(path.join(OUT,name+'.rgba'),buf);fs.writeFileSync(path.join(OUT,name+'.json'),JSON.stringify({region,n,sh:shs}));console.log(name,n)}
const BOSS=[120,150,540,500];
const bossLayers=()=>{c.STATIC=empty();c.RIM=ORIG.RIM;c.units=ORIG.units};
job('idle1','idle',6,BOSS,bossLayers);
job('hurt','hurt',DUR.hurt,BOSS);
job('mind1','mind',DUR.mind,BOSS);
job('eye','eye',DUR.eye,BOSS);
job('beam','beam',DUR.beam,BOSS);
// The body with the third eye open becomes the body for the rest
c.STATIC=empty();c.RIM=empty();c.units=[];
{const {o}=frame('eye',DUR.eye);const B2=empty();for(let i=0;i<W*H;i++){if(!o[i*4+3])continue;for(let q=0;q<4;q++)B2.d[i*4+q]=o[i*4+q];const X=i%W,Y=(i/W)|0;if(X<B2.x0)B2.x0=X;if(X>B2.x1)B2.x1=X;if(Y<B2.y0)B2.y0=Y;if(Y>B2.y1)B2.y1=Y}c.BODY=B2}
job('idle3','idle',6,BOSS,bossLayers);
job('mind3','mind',DUR.mind,BOSS);
job('death','death',DUR.death,BOSS);
// Effects on their own, no arena, no body
const none=()=>{c.STATIC=empty();c.RIM=empty();c.units=[];c.BODY=empty()};
c.state.spot=0;job('strike','sprout',DUR.sprout,[160,200,272,350],none);
c.hero=()=>{};
LAYER='back';job('grasp_back','grasp',DUR.grasp,[280,350,380,450],none);
LAYER='front';job('grasp_front','grasp',DUR.grasp,[280,350,380,450]);
c.text=()=>{};c.HPB=null;c.LB=null;
LAYER='back';job('cleanse_back','cleanse',DUR.cleanse,[200,180,460,290]);
LAYER='front';job('cleanse_front','cleanse',DUR.cleanse,[200,180,460,290]);
{const L=ORIG.STATIC,w=L.x1-L.x0+1,h=L.y1-L.y0+1,b=Buffer.alloc(w*h*4);for(let j=0;j<h;j++)for(let i=0;i<w;i++)for(let q=0;q<4;q++)b[(j*w+i)*4+q]=L.d[((L.y0+j)*W+L.x0+i)*4+q];fs.writeFileSync(path.join(OUT,'pit.rgba'),b);fs.writeFileSync(path.join(OUT,'pit.json'),JSON.stringify([L.x0,L.y0,w,h]))}
fs.writeFileSync(path.join(OUT,'meta.json'),JSON.stringify({units:ORIG.units.map(u=>[u.hx,u.hy,u.rx]),eyeAt:(c.BODY=ORIG.BODY,c.eyeAt()),eyes:c.eyes,top:c.top,spot0:C.SPOTS[0],grasp:[328,428],bar:[168+Math.round(320/2-64),216+14]}));
console.log('done');
