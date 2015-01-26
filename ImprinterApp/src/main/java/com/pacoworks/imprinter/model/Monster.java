package com.pacoworks.imprinter.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paco on 25/01/2015.
 * See LICENSE.md
 */
public class Monster extends Card{
    public String description;

    public List<MonsterRow> positions = new ArrayList<>();

    @Override
    public String alternativeFileName() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < positions.size(); i++) {
            MonsterRow monster = positions.get(i);
            builder.append(monster.name.replaceAll("[^\\w\\s]", ""));
            if (i + 1 != positions.size() ){
                builder.append("_");
            }
        }
        return builder.toString();
    }
}
