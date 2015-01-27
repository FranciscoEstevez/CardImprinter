
package com.pacoworks.imprinter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.pacoworks.imprinter.model.Character;
import com.pacoworks.imprinter.model.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paco on 25/01/2015. See LICENSE.md
 */
public class CharacterImprinter {
    public static final String CHARACTER_REGEX = "(.*) \\| (.*) \\((.*)\\)( \\[(.*)\\])?";

    public static final String CHARACTER_REPLACE = "]\\},\n\\{ \"name\" : \"$1\", \"archetype\" : \"$2\", \"filename\": \"$5\",  \"initiative\": $3, \"skills\" : [";

    public static final String SKILL_REGEX = "(..?)x (.*) -- ([^\\[\n]+)";

    public static final String SKILL_REGEX2 = "@@@(\\[(.*)\\])";

    public static final String SKILL_REGEX3 = "@@@";

    public static final String SKILL_REPLACE2 = " \"filename\": \"$2\" },";

    public static final String SKILL_REPLACE3 = " \"filename\": \"\" },";

    public static final String SKILL_REPLACE = "{ \"name\" : \"$2\", \"description\" : \"$3\", \"quantity\" : $1, @@@";

    public static final String CHARACTERS_REGEX_BOTTOM = ",\\Z";

    public static final String CHARACTERS_REGEX_BOTTOM_REPLACE = "]}]}";

    public static final String CHARACTERS_REGEX_TOP = "\\A\\]\\},\n";

    public static final String CHARACTERS_REGEX_TOP_REPLACE = "{ \"characters\" : [";

    public static final String EFFECTS_REGEX_1 = "(.*) -- ([^\\[\n]+)";

    public static final String EFFECTS_SUBST_1 = "{ \"name\": \"$1\", \"description\": \"$2\", @@@";

    public static final String EFFECTS_REGEX_2 = "@@@(\\[(.*)\\])";

    public static final String EFFECTS_SUBST_2 = "\"filename\" : \"$2\" },";

    public static final String EFFECTS_REGEX_3 = "@@@";

    public static final String EFFECTS_SUBST_3 = "\"filename\" : \"\" },";

    public static final String EFFECTS_REGEX_4 = ",\\Z";

    public static final String EFFECTS_SUBST_4 = "]}";

    public static final String EFFECTS_REGEX_5 = "\\A";

    public static final String EFFECTS_SUBST_5 = "{ \"effects\" : [";

    private Activity activity;

    private HeroHolder heroVHolder;

    private SkillHolder skillVHolder;

    private MonsterHolder monsterVHolder;

    private EffectHolder effectVHolder;

    private Typeface casablanca;

    private Typeface deutsch;

    private Typeface warpriest;

    private Typeface warpriest3D;

    private final Gson reader;

    public CharacterImprinter(Activity activity) {
        this.activity = activity;
        casablanca = Typeface.createFromAsset(activity.getAssets(), "casablanca.ttf");
        deutsch = Typeface.createFromAsset(activity.getAssets(), "deutsch.ttf");
        warpriest = Typeface.createFromAsset(activity.getAssets(), "warpriest.ttf");
        warpriest3D = Typeface.createFromAsset(activity.getAssets(), "warpriest3d.ttf");
        setupHero();
        setupSkill();
        setupMonster();
        setupEffect();
        reader = new Gson();
    }

    // SETUP
    // ////////////
    private void setupEffect() {
        effectVHolder = new EffectHolder();
        View holder = activity.findViewById(R.id.card_effect_include);
        effectVHolder.holder = holder;
        effectVHolder.description = (TextView)holder.findViewById(R.id.effect_description);
        effectVHolder.name = (TextView)holder.findViewById(R.id.effect_name);
        effectVHolder.name.setTypeface(casablanca, Typeface.BOLD);
        effectVHolder.name.setPaintFlags(effectVHolder.name.getPaintFlags()
                | Paint.SUBPIXEL_TEXT_FLAG);
        effectVHolder.description.setTypeface(casablanca, Typeface.BOLD);
        effectVHolder.description.setPaintFlags(effectVHolder.description.getPaintFlags()
                | Paint.SUBPIXEL_TEXT_FLAG);
    }

    private void setupMonster() {
        monsterVHolder = new MonsterHolder();
        View holder = activity.findViewById(R.id.card_monster_include);
        monsterVHolder.holder = holder;
        monsterVHolder.description = (TextView)holder.findViewById(R.id.monster_description);
        monsterVHolder.names.add((TextView)holder.findViewById(R.id.monster_row_1_name));
        monsterVHolder.names.add((TextView)holder.findViewById(R.id.monster_row_2_name));
        monsterVHolder.names.add((TextView)holder.findViewById(R.id.monster_row_3_name));
        monsterVHolder.names.add((TextView)holder.findViewById(R.id.monster_row_4_name));
        monsterVHolder.lives.add((TextView)holder.findViewById(R.id.monster_row_1));
        monsterVHolder.lives.add((TextView)holder.findViewById(R.id.monster_row_2));
        monsterVHolder.lives.add((TextView)holder.findViewById(R.id.monster_row_3));
        monsterVHolder.lives.add((TextView)holder.findViewById(R.id.monster_row_4));
        for (int i = 0; i < monsterVHolder.lives.size(); i++) {
            TextView name = monsterVHolder.names.get(i);
            TextView life = monsterVHolder.lives.get(i);
            name.setTypeface(deutsch, Typeface.BOLD);
            name.setPaintFlags(skillVHolder.name.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            life.setTypeface(deutsch, Typeface.BOLD);
            life.setPaintFlags(life.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        monsterVHolder.description.setTypeface(casablanca, Typeface.BOLD);
        monsterVHolder.description.setPaintFlags(monsterVHolder.description.getPaintFlags()
                | Paint.SUBPIXEL_TEXT_FLAG);
    }

    private void setupSkill() {
        skillVHolder = new SkillHolder();
        View holder = activity.findViewById(R.id.card_skill_include);
        skillVHolder.holder = holder;
        skillVHolder.description = (TextView)holder.findViewById(R.id.skill_description);
        skillVHolder.name = (TextView)holder.findViewById(R.id.skill_name);
        skillVHolder.owner = (TextView)holder.findViewById(R.id.skill_owner);
        skillVHolder.name.setTypeface(casablanca, Typeface.BOLD);
        skillVHolder.name.setPaintFlags(skillVHolder.name.getPaintFlags()
                | Paint.SUBPIXEL_TEXT_FLAG);
        skillVHolder.description.setTypeface(casablanca, Typeface.BOLD);
        skillVHolder.description.setPaintFlags(skillVHolder.description.getPaintFlags()
                | Paint.SUBPIXEL_TEXT_FLAG);
        skillVHolder.owner.setTypeface(deutsch, Typeface.BOLD);
        skillVHolder.owner.setPaintFlags(skillVHolder.owner.getPaintFlags()
                | Paint.SUBPIXEL_TEXT_FLAG);
    }

    private void setupHero() {
        heroVHolder = new HeroHolder();
        View holder = activity.findViewById(R.id.card_hero_include);
        heroVHolder.holder = holder;
        heroVHolder.archetype = (TextView)holder.findViewById(R.id.hero_archetype);
        heroVHolder.initiative = (TextView)holder.findViewById(R.id.hero_initiative);
        heroVHolder.name = (TextView)holder.findViewById(R.id.hero_name);
        heroVHolder.hp.add((ImageView)holder.findViewById(R.id.hero_hp_1));
        heroVHolder.hp.add((ImageView)holder.findViewById(R.id.hero_hp_2));
        heroVHolder.hp.add((ImageView)holder.findViewById(R.id.hero_hp_3));
        heroVHolder.hp.add((ImageView)holder.findViewById(R.id.hero_hp_4));
        heroVHolder.hp.add((ImageView)holder.findViewById(R.id.hero_hp_5));
        heroVHolder.hp.add((ImageView)holder.findViewById(R.id.hero_hp_6));
        heroVHolder.hp.add((ImageView)holder.findViewById(R.id.hero_hp_7));
        heroVHolder.hp.add((ImageView)holder.findViewById(R.id.hero_hp_8));
        heroVHolder.hp.add((ImageView)holder.findViewById(R.id.hero_hp_9));
        heroVHolder.skillsHolder = (ViewGroup)holder.findViewById(R.id.hero_skills_holder);
        heroVHolder.name.setTypeface(deutsch, Typeface.BOLD);
        heroVHolder.initiative.setTypeface(deutsch, Typeface.BOLD);
        heroVHolder.archetype.setTypeface(deutsch);
        heroVHolder.name.setPaintFlags(heroVHolder.name.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        heroVHolder.initiative.setPaintFlags(heroVHolder.initiative.getPaintFlags()
                | Paint.SUBPIXEL_TEXT_FLAG);
        heroVHolder.archetype.setPaintFlags(heroVHolder.archetype.getPaintFlags()
                | Paint.SUBPIXEL_TEXT_FLAG);
    }

    // PRINT
    // ////////////
    public void printMonsters(String path) throws IOException, JsonIOException, JsonSyntaxException {
        InputStreamReader file = new InputStreamReader(activity.getAssets().open("monsters.json"));
        Monsters monsters = reader.fromJson(file, Monsters.class);
        monsterVHolder.holder.setDrawingCacheEnabled(true);
        for (Monster monster : monsters.monsters) {
            int diff = monsterVHolder.lives.size() - monster.positions.size();
            for (int i = 0; i < monsterVHolder.lives.size(); i++) {
                TextView life = monsterVHolder.lives.get(i);
                TextView name = monsterVHolder.names.get(i);
                String lifeValue = "";
                String nameValue = "";
                if (i >= diff) {
                    MonsterRow monsterRow = monster.positions.get(i - diff);
                    lifeValue = monsterRow.life + "";
                    nameValue = monsterRow.name;
                }
                life.setText(lifeValue);
                name.setText(nameValue);
            }
            monsterVHolder.description.setText(monster.description);
            monsterVHolder.holder.buildDrawingCache();
            Bitmap card = monsterVHolder.holder.getDrawingCache();
            if (card != null) {
                createBitmap(card, monster.getFilename(), "monsters");
            }
        }
        monsterVHolder.holder.setDrawingCacheEnabled(false);
    }

    public void printEffects(String path) throws IOException, JsonIOException, JsonSyntaxException {
        String content = Files.toString(new File(path), Charset.defaultCharset());
        String step1 = content.replaceAll(EFFECTS_REGEX_1, EFFECTS_SUBST_1);
        String step2 = step1.replaceAll(EFFECTS_REGEX_2, EFFECTS_SUBST_2);
        String step3 = step2.replaceAll(EFFECTS_REGEX_3, EFFECTS_SUBST_3);
        String step4 = step3.replaceAll(EFFECTS_REGEX_4, EFFECTS_SUBST_4);
        String step5 = step4.replaceAll(EFFECTS_REGEX_5, EFFECTS_SUBST_5);
        /* Fuck you Android */
        String sanitized = step5.replace("null", "");
        DungeonEffects effects = reader.fromJson(sanitized, DungeonEffects.class);
        effectVHolder.holder.setDrawingCacheEnabled(true);
        for (DungeonEffect effect : effects.effects) {
            effectVHolder.name.setText(effect.name);
            effectVHolder.description.setText(effect.description);
            effectVHolder.holder.buildDrawingCache();
            Bitmap card = effectVHolder.holder.getDrawingCache();
            if (card != null) {
                createBitmap(card, effect.getFilename(), "effects");
            }
        }
        effectVHolder.holder.setDrawingCacheEnabled(false);
    }

    public void printCharacters(String path) throws IOException, JsonIOException,
            JsonSyntaxException {
        String content = Files.toString(new File(path), Charset.defaultCharset());
        String step1 = content.replaceAll(CHARACTER_REGEX, CHARACTER_REPLACE);
        String step2 = step1.replaceAll(SKILL_REGEX, SKILL_REPLACE);
        String step2a = step2.replaceAll(SKILL_REGEX2, SKILL_REPLACE2);
        String step2b = step2a.replaceAll(SKILL_REGEX3, SKILL_REPLACE3);
        String step3 = step2b.replaceAll(CHARACTERS_REGEX_BOTTOM, CHARACTERS_REGEX_BOTTOM_REPLACE);
        String step4 = step3.replaceAll(CHARACTERS_REGEX_TOP, CHARACTERS_REGEX_TOP_REPLACE);
        /* Fuck you Android */
        String sanitized = step4.replace("null", "");
        Characters characters = reader.fromJson(sanitized, Characters.class);
        heroVHolder.holder.setDrawingCacheEnabled(true);
        for (Character character : characters.characters) {
            /*
             * Fuck you Android and GSON, this should come up during parsing but Android is not
             * happy with new line searches, and GSON thinks it's still okay to create a null.
             */
            if (null == character.skills.get(character.skills.size() - 1)) {
                character.skills.remove(character.skills.size() - 1);
            }
            heroVHolder.name.setText(character.name);
            heroVHolder.archetype.setText(character.archetype);
            heroVHolder.initiative.setText(character.initiative + "");
            int hp = character.getHP();
            for (int i = 0; i < heroVHolder.hp.size(); i++) {
                heroVHolder.hp.get(i).setVisibility(hp < i + 1 ? View.INVISIBLE : View.VISIBLE);
            }
            LayoutInflater layoutInflater = activity.getLayoutInflater();
            heroVHolder.skillsHolder.removeAllViews();
            for (Skill skill : character.skills) {
                View skillView = layoutInflater.inflate(R.layout.hero_skill,
                        heroVHolder.skillsHolder, false);
                TextView quantity = (TextView)skillView.findViewById(R.id.hero_skill_quantity);
                TextView description = (TextView)skillView
                        .findViewById(R.id.hero_skill_description);
                quantity.setPaintFlags(quantity.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
                description.setPaintFlags(description.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
                quantity.setTypeface(casablanca, Typeface.BOLD);
                description.setTypeface(casablanca, Typeface.BOLD);
                quantity.setText(skill.quantity + "");
                description.setText(skill.name);
                heroVHolder.skillsHolder.addView(skillView);
                printSkill(character.name, character.archetype, character.getFilename(), skill);
            }
            heroVHolder.holder.measure(
                    View.MeasureSpec.makeMeasureSpec(310, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(202, View.MeasureSpec.EXACTLY));
            heroVHolder.holder.layout(0, 0, heroVHolder.holder.getMeasuredWidth(),
                    heroVHolder.holder.getMeasuredHeight());
            heroVHolder.holder.buildDrawingCache(true);
            Bitmap card = heroVHolder.holder.getDrawingCache();
            if (card != null) {
                createBitmap(card, character.getFilename(), "characters");
            }
        }
        heroVHolder.holder.setDrawingCacheEnabled(false);
    }

    private void printSkill(String owner, String archetype, String parentFilename, Skill skill)
            throws IOException {
        skillVHolder.holder.setDrawingCacheEnabled(true);
        skillVHolder.description.setText(skill.description);
        skillVHolder.name.setText(skill.name);
        skillVHolder.owner.setText(owner + " · " + archetype);
        skillVHolder.holder.buildDrawingCache(true);
        Bitmap card = skillVHolder.holder.getDrawingCache();
        if (card != null) {
            createBitmap(card, parentFilename + "_" + skill.getFilename(), "skills/"
                    + parentFilename);
        }
        skillVHolder.holder.setDrawingCacheEnabled(false);
    }

    private void createBitmap(Bitmap card, String name, String folder) throws IOException {
        String dir = Environment.getExternalStorageDirectory().toString() + "/CardImprinter/"
                + folder + "/";
        File folderFile = new File(dir);
        folderFile.mkdirs();
        OutputStream fos = null;
        File output = new File(dir, name + ".png");
        fos = new FileOutputStream(output);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        card.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bos.flush();
        bos.close();
    }

    private class HeroHolder {
        private View holder;

        private TextView initiative;

        private TextView name;

        private TextView archetype;

        private List<ImageView> hp = new ArrayList<>();

        private ViewGroup skillsHolder;
    }

    private class SkillHolder {
        private View holder;

        private TextView description;

        private TextView name;

        public TextView owner;
    }

    private class MonsterHolder {
        private View holder;

        private TextView description;

        private List<TextView> lives = new ArrayList<>();

        private List<TextView> names = new ArrayList<>();
    }

    private class EffectHolder {
        private View holder;

        private TextView description;

        private TextView name;
    }
}
