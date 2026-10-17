package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.badlogic.gdx.utils.Timer;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Arena;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AlmostEmptySprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.utils.Callback;
import com.watabou.utils.Reflection;

import java.util.ArrayList;


public class WndDialogueWithPic extends Window {
    public Image icon;


    private RenderedTextBlock ttl;
    private RenderedTextBlock tf;
    private static final int MARGIN = 2;

    ArrayList<Runnable> runnableArrayList;


    public int textNum = 0;
    public boolean lastDialogue = false;
    public boolean unskippable = false;

    private String[] texts;
    private String curText;
    private int letterNum = 0;
    private CharSprite image;
    private boolean typing = false;
    private boolean shouldSkipThisRunnable = false;

    public byte spriteActionIndexes[];

    public static final byte IDLE = 0;
    public static final byte RUN = 1;
    public static final byte ATTACK = 2;
    public static final byte DIE = 3;

    public enum WndType {
        NORMAL,
        ALWAYS_SHOW,
        FINAL, //ends the game after that dialogue
        YOGFINAL // unskippable
    }

    public static void dialogue(CharSprite icon, String title, String[] text) {
        dialogue(icon, title, text, new byte[]{});
    }
    public static void dialogue(CharSprite icon, String title, String[] text, WndType type) {
        dialogue(icon, title, text, new byte[]{}, type, new ArrayList<>());
    }

    public static void dialogue(CharSprite icon, String title, String[] text, byte spriteActionIndexes[]) {
        dialogue(icon, title, text, spriteActionIndexes, WndType.NORMAL, new ArrayList<>());
    }
    public static void dialogue(CharSprite icon, String title, String[] text, byte spriteActionIndexes[], WndType type) {
        dialogue(icon, title, text, spriteActionIndexes, type, new ArrayList<>());
    }
    public static void dialogue(CharSprite icon, String title, String[] text, byte[] spriteActionIndexes, WndType type, ArrayList<Runnable> runnableArrayList){
        dialogue(icon, title, text, spriteActionIndexes, type, runnableArrayList, 0);
    }
    public static void dialogueFromWindowThatWasThereButDisappearedDueToResizing(WndDialogueWithPic wndref){
        wndref.timer.clear();
        if ( level == null || level.mode == WndModes.Modes.NORMAL || wndref.lastDialogue) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    CharSprite newSprite = Reflection.newInstance(wndref.image.getClass());
                    if (newSprite == null){
                      newSprite = wndref.image;
                      newSprite.scale.set(1);
                      newSprite.x= 0;
                      newSprite.y = 0;
                      newSprite.update();
                    }

                    WndDialogueWithPic wnd = new WndDialogueWithPic(
                            newSprite, wndref.ttl.text(), wndref.texts, wndref.spriteActionIndexes, wndref.textNum, true);
                    wnd.repositionElements();
                    wnd.lastDialogue = wndref.lastDialogue;
                    wnd.unskippable = wndref.unskippable;
                    wnd.runnableArrayList = wndref.runnableArrayList;
                    Dungeon.level.lastSavedWndDialogueWithPic = wnd;
                    if (ShatteredPixelDungeon.scene() instanceof GameScene)
                        GameScene.show(wnd);
                    else ShatteredPixelDungeon.scene().add(wnd);
                }
            });
        }
    }
    public static void dialogue(CharSprite icon, String title, String[] text, byte[] spriteActionIndexes, WndType type, ArrayList<Runnable> runnableArrayList, int textnum) {
        if ( level == null || level.mode == WndModes.Modes.NORMAL || type == WndType.FINAL || type == WndType.YOGFINAL || type == WndType.ALWAYS_SHOW) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    WndDialogueWithPic wnd = new WndDialogueWithPic(icon, title, text, spriteActionIndexes ,textnum, false);
                    if (type == WndType.FINAL) wnd.lastDialogue = true;
                    if (type == WndType.YOGFINAL) wnd.unskippable = true;
                    wnd.runnableArrayList = runnableArrayList;
                    Dungeon.level.lastSavedWndDialogueWithPic = wnd;
                    if (ShatteredPixelDungeon.scene() instanceof GameScene)
                        GameScene.show(wnd);
                    else ShatteredPixelDungeon.scene().add(wnd);
                }
            });
        }
    }
    public static void dialogue_outsideOfGame(CharSprite icon, String title, String[] text, byte[] spriteActionIndexes) {
        WndDialogueWithPic wnd = new WndDialogueWithPic(icon, title, text, spriteActionIndexes);
        ShatteredPixelDungeon.scene().add(wnd);
    }


    public WndDialogueWithPic(CharSprite icon, String title, String[] text) {
        this(icon, title, text, new byte[]{});
    }

    public WndDialogueWithPic(CharSprite icon, String title, String[] text, byte spriteActionIndexes[]) {
        this(icon, title, text, spriteActionIndexes, 0, false);
    }

    public WndDialogueWithPic(CharSprite icon, String title, String[] text, byte spriteActionIndexes[], int textnum, boolean shouldSkipThisRunnable) {
        super(0, 0, Chrome.get(Chrome.Type.TOAST_TR));
        shadow.visible = false;
        resize(PixelScene.uiCamera.width, PixelScene.uiCamera.height);
        texts = text;



        //FIXME there is somewhere a null icon, i need to find where
        if (icon == null) {
            AlmostEmptySprite sprite = new AlmostEmptySprite();
            image = sprite;
        } else image = icon;

        this.spriteActionIndexes = spriteActionIndexes;

        ttl = PixelScene.renderTextBlock(title, 11);
        tf = PixelScene.renderTextBlock("", 9);
        repositionElements();
        addToFront(chrome);
        addToBack(image);
        add(ttl);
        add(tf);

        this.shouldSkipThisRunnable = shouldSkipThisRunnable;

        this.textNum = textnum;


        PointerArea blocker = new PointerArea(0, 0, PixelScene.uiCamera.width, PixelScene.uiCamera.height) {
            @Override
            protected void onClick(PointerEvent event) {
                onBackPressed();
            }
        };
        blocker.camera = PixelScene.uiCamera;
        addToBack(blocker);
        startText(texts[textNum]);
    }
    private void repositionElements(){
        int chromeWidth = PixelScene.landscape() ? (int)(PixelScene.uiCamera.width / 1.5f): PixelScene.uiCamera.width - 4;
        int chromeHeight = Math.round(PixelScene.uiCamera.height * 0.3f);
        chrome.x = (PixelScene.uiCamera.width - chromeWidth) * 0.5f;
        chrome.y = (PixelScene.uiCamera.height - chromeHeight - 2);
        chrome.size(chromeWidth, chromeHeight);

        float y = chrome.y + MARGIN;

        int scale = 8;
        image.scale.set(scale);

        while (image.width() > PixelScene.uiCamera.width / 2) {
            scale--;
            image.scale.set(scale);
        }

        image.x = chrome.x;
        image.y = chrome.y - image.height * (scale / 1.5f);

        ttl.maxWidth(chromeWidth - 4 * MARGIN);
        ttl.setPos(chrome.x + image.width() + 2 * MARGIN, chrome.y - 2 * MARGIN - ttl.height());

        tf.maxWidth(chromeWidth - 4 * MARGIN);
        tf.setPos(chrome.x + 2 * MARGIN, y + 2 * MARGIN);
    }


    private Timer timer = new Timer();




    @Override
    public void onBackPressed() {
        if (!unskippable) {
            if (typing) {
                typing = false;
                timer.stop();
                timer.clear();
                tf.text(texts[textNum]);
            } else {
                textNum++;
                if (textNum >= texts.length) {
                    hide();
                    nullifyLastWindow();
                    if (lastDialogue) {
                        Arena.completeStage();
                        return;
                    }
                } else startText(texts[textNum]);
            }
            update();
        }

    }

    @Override
    public void hide() {
        super.hide();
        timer.stop();
        timer.clear();
    }
    public static void nullifyLastWindow(){
        if (level!=null) level.lastSavedWndDialogueWithPic = null;
    }

    private void startText(String text) {
        curText = "";
        tf.text(curText);
        update();
        letterNum = 0;
        typing = true;
        timer.clear();
        timer.start();
        if (shouldSkipThisRunnable){
            shouldSkipThisRunnable = false;
        } else {
            try {
                runnableArrayList.get(textNum).run();
            } catch (Exception ignored){}
        }
        if (textNum < spriteActionIndexes.length) switch (spriteActionIndexes[textNum]) {
            case 0:
            default:
                image.play(image.idle);
                break;
            case 1:
                image.play(image.run);
                break;
            case 2:
                image.play(image.attack);
                break;
            case 3:
                image.play(image.die);
                break;
        }
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (typing) nextLetter(text);
                WndDialogueWithPic.this.update();
            }
        }, 0f, 0.04f, text.length());

    }

    private void nextLetter(String text) {
        curText += text.charAt(letterNum);
        tf.text(curText);
        letterNum++;
        if (letterNum >= text.length()) {
            typing = false;
            timer.stop();
            timer.clear();
        }
    }

}
