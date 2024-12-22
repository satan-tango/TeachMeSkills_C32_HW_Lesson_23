package com.varkovich.lesson_23.model;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@ToString
public class Root {

    @XmlElementWrapper
    @XmlElement(name = "student")
    private List<Book> bookList;
}
