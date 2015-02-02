
package com.pacoworks.imprinter.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paco on 25/01/2015. See LICENSE.md
 */
public class MonsterGroup extends Card {
    public String description;

    public List<Monster> enemies = new ArrayList<>();

    public MonsterGroup() {
    }

    @Override
    protected String alternativeUnsanitizedFileName() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < enemies.size(); i++) {
            Monster monster = enemies.get(i);
            builder.append(monster.name);
            if (i + 1 != enemies.size()) {
                builder.append("_");
            }
        }
        return builder.toString();
    }
}
