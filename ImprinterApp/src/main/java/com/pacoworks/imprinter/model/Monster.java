package com.pacoworks.imprinter.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paco on 25/01/2015.
 * See LICENSE.md
 */
public class Monster extends Card{
    public String description;

    public List<MosterRow> positions = new ArrayList<>();

    @Override
    public String alternativeFileName() {
        if (positions.size() > 0){
            return positions.get(positions.size() -1).name;
        }
        return toString();
    }
}
