package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/authenticated")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(RoleService roleService,UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping
    public String adminIndex(){
        return "admin";
    }

    @GetMapping("/admin")
    public String getUsers(Model model) {
        model.addAttribute("userList", userService.userList());
        return "users";
    }

    @GetMapping("admin/new")
    public String newUser(Model model) {
        model.addAttribute(new User());
        Set<Role> roles = roleService.getRoleList();
        model.addAttribute("allRoles", roles);
        return "new";
    }

    @PostMapping("/admin/createUser")
    public String create(@ModelAttribute("user")User user, BindingResult bindingResult,
                         @RequestParam("role_authorities") List<String> role_value) {
        if (bindingResult.hasErrors())
            return "new";
        user.setRoles(userService.getSetOfRoles(role_value));
        userService.saveUser(user);
        return "redirect:/authenticated/admin";
    }
    @GetMapping("/admin/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("allRoles", roleService.getRoleList());
        return "edit";
    }

    @PatchMapping("/admin/{id}")
    public String update(@ModelAttribute("user")User user, BindingResult bindingResult, @PathVariable("id") int id,
                         @RequestParam ("role_authorities") List<String> role_value) {
        if (bindingResult.hasErrors())
            return "edit";
        user.setRoles(userService.getSetOfRoles(role_value));
        userService.update(id, user);
        return "redirect:/authenticated/admin";
    }

    @DeleteMapping("/admin/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.deleteById(id);
        return "redirect:/authenticated/admin";
    }
}
