package vn.hoidanit.laptopshop.controller.admin;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.UploadFileService;
import vn.hoidanit.laptopshop.service.UserService;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;

@Controller
public class UserController {

    private UserService userService;
    private UploadFileService uploadFileService;
    private final ServletContext servletContext;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, ServletContext servletContext, UploadFileService uploadFileService,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.servletContext = servletContext;
        this.uploadFileService = uploadFileService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping("/")
    public String getHomePage(Model model) {
        String test = this.userService.getHomePage();
        model.addAttribute("test", test);
        return "hello";
    }

    @RequestMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> listUser = this.userService.getAllUsers();
        model.addAttribute("listUser", listUser);
        return "admin/user/show";
    }

    @GetMapping("/admin/user/create")
    public String getCreateUser(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @PostMapping("/admin/user/create")
    public String createUserPage(Model model, @ModelAttribute("newUser") User duong3d,
            @RequestParam("hoidanitFile") MultipartFile file) {

        String avatar = this.uploadFileService.handleSaveUploadFile(file, "avatar");
        String hashPassword = this.passwordEncoder.encode(duong3d.getPassword());
        duong3d.setAvatar(avatar);
        duong3d.setPassword(hashPassword);
        duong3d.setRole(this.userService.getRoleByName(duong3d.getRole().getName()));
        this.userService.saveUser(duong3d);
        return "redirect:/admin/user";
    }

    @RequestMapping("/admin/user/{id}")
    public String showUser(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/user/detail";
    }

    @RequestMapping("/admin/user/update/{id}")
    public String getUpdateUser(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("updateUser", user);
        return "admin/user/update";
    }

    @PostMapping("/admin/user/update")
    public String updateUser(Model model, @ModelAttribute("updateUser") User duong3d) {
        User currentUser = this.userService.getUserById(duong3d.getId());
        if (currentUser != null) {
            currentUser.setFullName(duong3d.getFullName());
            currentUser.setAddress(duong3d.getAddress());
            currentUser.setPhone(duong3d.getPhone());
            this.userService.saveUser(currentUser);
        }
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/delete/{id}")
    public String getDeleteUser(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("deleteUser", user);
        return "admin/user/delete";
    }

    @PostMapping("/admin/user/delete")
    public String deleteUser(Model model, @ModelAttribute("deleteUser") User duong3d) {
        this.userService.deleteUserById(duong3d.getId());
        return "redirect:/admin/user";
    }
}
