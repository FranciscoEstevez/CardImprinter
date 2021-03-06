
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
import com.pacoworks.imprinter.model.Character;
import com.pacoworks.imprinter.model.*;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paco on 25/01/2015. See LICENSE.md
 */
public class CharacterImprinter {
    private Activity activity;

    private HeroHolder heroVHolder;

    private SkillHolder skillVHolder;

    private MonsterHolder monsterVHolder;

    private EffectHolder effectVHolder;

    private final Typeface casablanca;

    private final Typeface deutsch;

    private final Typeface warpriest;

    private final Typeface warpriest3D;

    private final Yaml yaml;

    public CharacterImprinter(Activity activity) {
        this.activity = activity;
        casablanca = Typeface.createFromAsset(activity.getAssets(), "casablanca.ttf");
        deutsch = Typeface.createFromAsset(activity.getAssets(), "deutsch.ttf");
        warpriest = Typeface.createFromAsset(activity.getAssets(), "warpriest.ttf");
        warpriest3D = Typeface.createFromAsset(activity.getAssets(), "warpriest3d.ttf");
        heroVHolder = setupHero();
        skillVHolder = setupSkill();
        monsterVHolder = setupMonster();
        effectVHolder = setupEffect();
        yaml = setupYaml();
    }

    private Yaml setupYaml() {
        Constructor constructor = new Constructor();
        TypeDescription character = new TypeDescription(Character.class);
        character.putListPropertyType("skills", Skill.class);
        character.setTag("!character");
        constructor.addTypeDescription(character);
        TypeDescription monsterGroup = new TypeDescription(MonsterGroup.class);
        monsterGroup.putListPropertyType("enemies", Monster.class);
        monsterGroup.setTag("!monster");
        constructor.addTypeDescription(monsterGroup);
        TypeDescription singleMonster = new TypeDescription(Monster.class);
        singleMonster.setTag("!single");
        constructor.addTypeDescription(singleMonster);
        TypeDescription effect = new TypeDescription(Effect.class);
        effect.setTag("!effect");
        constructor.addTypeDescription(effect);
        TypeDescription skill = new TypeDescription(Skill.class);
        skill.setTag("!skill");
        constructor.addTypeDescription(skill);
        TypeDescription item = new TypeDescription(Item.class);
        item.setTag("!item");
        constructor.addTypeDescription(item);
        Representer representer = new Representer();
        representer.addClassTag(MonsterGroup.class, new Tag("!monster"));
        representer.addClassTag(Character.class, new Tag("!character"));
        representer.addClassTag(Effect.class, new Tag("!effect"));
        representer.addClassTag(Skill.class, new Tag("!skill"));
        representer.addClassTag(Monster.class, new Tag("!single"));
        representer.addClassTag(Item.class, new Tag("!item"));
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setPrettyFlow(true);
        dumperOptions.setIndent(4);
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setWidth(Integer.MAX_VALUE);
        return new Yaml(constructor, representer, dumperOptions);
    }

    // SETUP
    // ////////////
    private EffectHolder setupEffect() {
        EffectHolder effectVHolder = new EffectHolder();
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
        return effectVHolder;
    }

    private MonsterHolder setupMonster() {
        MonsterHolder monsterVHolder = new MonsterHolder();
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
        return monsterVHolder;
    }

    private SkillHolder setupSkill() {
        SkillHolder skillVHolder = new SkillHolder();
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
        return skillVHolder;
    }

    private HeroHolder setupHero() {
        HeroHolder heroVHolder = new HeroHolder();
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
        return heroVHolder;
    }

    private void printMonster(MonsterGroup monsterGroup) throws IOException {
        monsterVHolder.holder.setDrawingCacheEnabled(true);
        int diff = monsterVHolder.lives.size() - monsterGroup.enemies.size();
        for (int i = 0; i < monsterVHolder.lives.size(); i++) {
            TextView life = monsterVHolder.lives.get(i);
            TextView name = monsterVHolder.names.get(i);
            String lifeValue = "";
            String nameValue = "";
            if (i >= diff) {
                Monster monster = monsterGroup.enemies.get(i - diff);
                lifeValue = monster.life + "";
                nameValue = monster.name;
            }
            life.setText(lifeValue);
            name.setText(nameValue);
        }
        monsterVHolder.description.setText(monsterGroup.description);
        monsterVHolder.holder.buildDrawingCache();
        Bitmap card = monsterVHolder.holder.getDrawingCache();
        if (card != null) {
            createBitmap(card, "monster_" + monsterGroup.getFilename(), "monsters");
        }
        monsterVHolder.holder.setDrawingCacheEnabled(false);
    }

    private void printCharacter(Character character) throws IOException {
        heroVHolder.holder.setDrawingCacheEnabled(true);
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
            View skillView = layoutInflater.inflate(R.layout.hero_skill, heroVHolder.skillsHolder,
                    false);
            TextView quantity = (TextView)skillView.findViewById(R.id.hero_skill_quantity);
            TextView description = (TextView)skillView.findViewById(R.id.hero_skill_description);
            quantity.setPaintFlags(quantity.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            description.setPaintFlags(description.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            quantity.setTypeface(casablanca, Typeface.BOLD);
            description.setTypeface(casablanca, Typeface.BOLD);
            quantity.setText(skill.quantity + "");
            description.setText(skill.name);
            heroVHolder.skillsHolder.addView(skillView);
            printSkill(character.name + " · " + character.archetype, character.getFilename(), skill);
        }
        heroVHolder.holder.measure(View.MeasureSpec.makeMeasureSpec(310, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(202, View.MeasureSpec.EXACTLY));
        heroVHolder.holder.layout(0, 0, heroVHolder.holder.getMeasuredWidth(),
                heroVHolder.holder.getMeasuredHeight());
        heroVHolder.holder.buildDrawingCache(true);
        Bitmap card = heroVHolder.holder.getDrawingCache();
        if (card != null) {
            createBitmap(card, character.getFilename(), "characters");
        }
        heroVHolder.holder.setDrawingCacheEnabled(false);
    }

    private void printEffect(Effect effect) throws IOException {
        effectVHolder.holder.setDrawingCacheEnabled(true);
        effectVHolder.name.setText(effect.name);
        effectVHolder.description.setText(effect.description);
        effectVHolder.holder.buildDrawingCache();
        Bitmap card = effectVHolder.holder.getDrawingCache();
        if (card != null) {
            createBitmap(card, "effect_" + effect.getFilename(), "effects");
        }
        effectVHolder.holder.setDrawingCacheEnabled(false);
    }

    private void printSkill(String userName, String userFilename, Skill skill) throws IOException {
        skillVHolder.holder.setDrawingCacheEnabled(true);
        skillVHolder.description.setText(skill.description);
        skillVHolder.name.setText(skill.name);
        skillVHolder.owner.setText(userName);
        skillVHolder.holder.buildDrawingCache(true);
        Bitmap card = skillVHolder.holder.getDrawingCache();
        if (card != null) {
            createBitmap(card, userFilename + "_" + skill.getFilename(), "skills/" + userFilename);
            createBitmap(card, userFilename + "_" + skill.getFilename(), "skills_dump");
        }
        skillVHolder.holder.setDrawingCacheEnabled(false);
    }

    private void printItem(Item item) throws IOException {
        skillVHolder.holder.setDrawingCacheEnabled(true);
        skillVHolder.description.setText(item.description);
        skillVHolder.name.setText(item.name);
        skillVHolder.owner.setText(item.user);
        skillVHolder.holder.buildDrawingCache(true);
        Bitmap card = skillVHolder.holder.getDrawingCache();
        if (card != null) {
            createBitmap(card, "item_" + item.getFilename(), "items");
        }
        skillVHolder.holder.setDrawingCacheEnabled(false);
    }

    private void createBitmap(Bitmap card, String name, String folder) throws IOException {
        OutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            String dir = Environment.getExternalStorageDirectory().toString() + "/CardImprinter/"
                    + folder + "/";
            File folderFile = new File(dir);
            folderFile.mkdirs();
            File output = new File(dir, name + ".png");
            fos = new FileOutputStream(output);
            bos = new BufferedOutputStream(fos);
            card.compress(Bitmap.CompressFormat.PNG, 100, bos);
        } finally {
            if (fos != null) {
                fos.close();
            }
            if (bos != null) {
                bos.flush();
                bos.close();
            }
        }
    }

    // PRINT
    // ////////////
    public void printAll(String path) throws IOException, YAMLException {
        FileInputStream stream = new FileInputStream(path);
        Iterable<Object> read = yaml.loadAll(stream);
        for (Object o : read) {
            if (o instanceof MonsterGroup) {
                printMonster((MonsterGroup)o);
            } else if (o instanceof Character) {
                printCharacter((Character)o);
            } else if (o instanceof Effect) {
                printEffect((Effect)o);
            } else if (o instanceof Item) {
                Item item = (Item)o;
                printItem(item);
            }
        }
    }

    private static class HeroHolder {
        private View holder;

        private TextView initiative;

        private TextView name;

        private TextView archetype;

        private List<ImageView> hp = new ArrayList<>();

        private ViewGroup skillsHolder;
    }

    private static class SkillHolder {
        private View holder;

        private TextView description;

        private TextView name;

        public TextView owner;
    }

    private static class MonsterHolder {
        private View holder;

        private TextView description;

        private List<TextView> lives = new ArrayList<>();

        private List<TextView> names = new ArrayList<>();
    }

    private static class EffectHolder {
        private View holder;

        private TextView description;

        private TextView name;
    }
}
