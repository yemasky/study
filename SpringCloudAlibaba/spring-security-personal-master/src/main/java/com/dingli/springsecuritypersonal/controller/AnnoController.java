package com.dingli.springsecuritypersonal.controller;

import com.dingli.springsecuritypersonal.entity.School;
import com.dingli.springsecuritypersonal.security.JWTProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AnnoController {

    @Autowired
    private JWTProvider jwtProvider;

    @RequestMapping("/annotation")
//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String info() {
        return "拥有admin权限";
    }

    @RequestMapping("/postAuthorize")
    @PreAuthorize("hasRole('ADMIN')")
    @PostAuthorize("returnObject.id%2==0")
    public School postAuthorize(Long id) {
        School school = new School();
        school.setId(id);
        return school;
    }

    @RequestMapping("/postFilter")
    @PreAuthorize("hasRole('ADMIN')")
    @PostFilter("filterObject.id%2==0")
    public List<School> postFilter() {
        List<School> schools = new ArrayList<School>();
        School school;
        for (int i = 0; i < 10; i++) {
            school = new School();
            school.setId((long)i);
            schools.add(school);
        }
        return schools;
    }

    @RequestMapping("/preFilter")
    @PreAuthorize("hasRole('ADMIN')")
    @PreFilter(filterTarget="ids", value="filterObject%2==0")
    public List<Long> preFilter(@RequestParam("ids") List<Long> ids) {
        return ids;
    }

}
