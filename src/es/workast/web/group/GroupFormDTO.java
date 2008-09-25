package es.workast.web.group;

import javax.validation.constraints.Size;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public class GroupFormDTO {

    // ---------- Properties

    @Size(min = 1, max = 50)
    private String name;

    // ---------- Accessors

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
