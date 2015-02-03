package ua.samosfator.moduleok;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import ua.samosfator.moduleok.parser.Semesters;

public class Student {

    private String mainPageHtml;

    private String name;
    private String surname;
    private String patronymic;

    private String group;

    private Semesters semesters;

    public Student(String mainPageHtml) {
        this.mainPageHtml = mainPageHtml;

        this.name = getName();
        this.surname = getSurName();
        this.patronymic = getPatronymic();

        this.group = getGroup();
        this.semesters = new Semesters(getMainPageDocument().html());
    }

    public String getNameSurname() {
        return getName() + " " + getSurName();
    }

    public String getGroupName() {
        return getGroup();
    }

    public Semesters getSemesters() {
        return semesters;
    }

    private String getName() {
        if (this.name != null) return this.name;
        return getNameParts()[1];
    }

    private String getSurName() {
        if (this.surname != null) return this.surname;
        return getNameParts()[0];
    }

    private String getPatronymic() {
        if (this.patronymic != null) return this.patronymic;
        return getNameParts()[2];
    }

    private String getGroup() {
        if (this.group != null) return this.group;
        return getRawGroupName().substring(0, getRawGroupName().length() - 1);
    }

    private String getRawGroupName() {
        return getPageTitle().split("\\(")[1];
    }

    private String[] getNameParts() {
        return getFullStudentName().split(" ");
    }

    private String getFullStudentName() {
        return capitalizeString(getPageTitle().split("\\(")[0].trim());
    }

    private String getPageTitle() {
        return getMainPageDocument().select(".pageTitle").text();
    }

    private Document getMainPageDocument() {
        return Jsoup.parse(mainPageHtml);
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') {
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    @Override
    public String toString() {
        return "Student{" +
                "surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
