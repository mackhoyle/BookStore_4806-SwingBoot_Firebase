package com.app.controllers;
import com.app.models.Book;
import com.app.services.FirebaseInitializer;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@RestController
public class BookController {
    @Autowired
    FirebaseInitializer db;
    List<Book> bookList = new ArrayList<Book>();


    @PostConstruct
    public void getAllBooks() throws InterruptedException, ExecutionException {
        List<Book> empList = new ArrayList<Book>();
        CollectionReference books = db.getFirebase().collection("Books");
        ApiFuture<QuerySnapshot> querySnapshot= books.get();
        for(DocumentSnapshot doc:querySnapshot.get().getDocuments()) {
            Book emp = Objects.requireNonNull(doc.toObject(Book.class)).withId(doc.getId());
            empList.add(emp);
        }
        bookList = empList;
    }

    @RequestMapping("/bookstore")
    public Object listAllBooks(Model model) {
        model.addAttribute("bookList", (List<Book>) bookList);
        ModelAndView mav = new ModelAndView("bookstore");
        return mav;
    }
}

