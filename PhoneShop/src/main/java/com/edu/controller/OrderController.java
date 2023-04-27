package com.edu.controller;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.edu.Utils.SessionUtil;
import com.edu.model.Order;
import com.edu.model.OrderDetail;
import com.edu.model.User;
import com.edu.model.Voucher;
import com.edu.reponsitory.OrderDetailReponsitory;
import com.edu.service.OrderDetailService;
import com.edu.service.OrderService;
import com.edu.service.UserService;

@Controller
public class OrderController {
    @Autowired
    OrderService orderservice;

    @Autowired
    private JavaMailSender mailsender;

    @Autowired
    HttpSession session;

    @Autowired
    UserService userService;

    @Autowired
    HttpServletRequest request;

    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    SessionUtil sessionUtil;

    @RequestMapping("/order/checkout")
    public String checkout(Model model) {
        String phone = userService.findID(request.getRemoteUser()).getPhone();
        System.out.println("phone: " + phone);
        session.setAttribute("phone", phone);
        return "order/checkout";
    }

    @RequestMapping("/order/list")
    public String list(Model model, HttpServletRequest request) {
        String username = request.getRemoteUser();
        model.addAttribute("orders", orderservice.findUsername(username));
        return "order/list";
    }

    @RequestMapping("/order/list_fail")
    public String listfail(Model model, HttpServletRequest request) {
        String username = request.getRemoteUser();
        model.addAttribute("orders", orderservice.findUsernamefail(username));
        return "order/list_fail";
    }

    @RequestMapping("/order/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        model.addAttribute("order", orderservice.findId(id));
        return "order/detail";
    }

    @RequestMapping("/order/details/{id}")
    public String details(@PathVariable("id") Long id, Model model)
            throws UnsupportedEncodingException, MessagingException {
        model.addAttribute("order", orderservice.findId(id));
        List<OrderDetail> detail = orderDetailService.findByOrderIdlist(id);
        Integer giamgia = sessionUtil.getAttribute("giamgia");
        User user = userService.findID(request.getRemoteUser());
        Order order = orderservice.findId(id);
//        for (OrderDetail orderDetail : detail) {
//            orderDetail.setTotal(detail.getQuantity()*detail.getPrice()+30000-giamgia);
//        }
        for (int i = 0; i < detail.size(); i++) {
            detail.get(i)
                    .setTotal((detail.get(i).getQuantity())
                            * (detail.get(i).getPrice()
                                    - (detail.get(i).getPrice() * detail.get(i).getProduct().getDiscount().getNumber()))
                            + 30000 - giamgia);
            detail.get(i).getProduct().setQuantity(detail.get(i).getProduct().getQuantity() - 1);
        }
        String email = user.getEmail();
        Long idorder = order.getId();
        if(user.getEmail() != null) {
            sendEmail(email,idorder);
        }

        model.addAttribute("pageTitle", "Information line");
        orderDetailService.saveAll(detail);

        return "order/detail";
    }

    private void sendEmail(String email,Long idorder)
            throws UnsupportedEncodingException, MessagingException {
        Order order = orderservice.findId(idorder);
     
        MimeMessage message = mailsender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("haidang17522@gmail.com", "Phone Store");
        helper.setTo(email);
        
        String subject = "Information line";
      
        String content = "-------------------------------------------------------------------------------------------------"
        +"<br>| Hello, your bill: " + order.getId() 
        
        + "<br>| on the day: " + order.getCreateDate() 
        + "<br>| Phone number: "+order.getPhone()
        + "<br>| address: " + order.getAddress() + order.getDistrict() + order.getProvince()
        + "<br>| Click to See details" 
        +"<br>-------------------------------------------------------------------------------------------------"
        +"<br><a href=\"http://localhost:8080/order/details/" + order.getId()
        + "\">See details</a>"; ;

        helper.setSubject(subject);

        helper.setText(content, true);

        mailsender.send(message);
    }
    
    private void sendEmailCancle(String email,Long idorder)
            throws UnsupportedEncodingException, MessagingException {
        Order order = orderservice.findId(idorder);
     
        MimeMessage message = mailsender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("haidang17522@gmail.com", "Phone Store");
        helper.setTo(email);
        
        String subject = "Information line";
      
        String content = "-------------------------------------------------------------------------------------------------"
        +"<br>| Hello, Canceled invoice: " + order.getId() 
        
        + "<br>| I wish you a good day, see you again"
        
        + "<br>| Click to See details" 
        +"<br>-------------------------------------------------------------------------------------------------"
        +"<br><a href=\"http://localhost:8080/order/details/" + order.getId()
        + "\">See details</a>"; ;

        helper.setSubject(subject);

        helper.setText(content, true);

        mailsender.send(message);
    }
    //
    @RequestMapping("/order/track/{id}")
    public String track(@PathVariable("id") Long id, Model model) {
        model.addAttribute("order", orderservice.findId(id));
        return "order/track";
    }

    @RequestMapping("/order/delete/{id}")
    public String delete(@PathVariable("id") Long id, Model model) throws UnsupportedEncodingException, MessagingException {
        Order o = orderservice.findById(id).get();
        o.setStatus(false);
        orderservice.save(o);
        List<OrderDetail> detail = orderDetailService.findByOrderIdlist(id);
        for (int i = 0; i < detail.size(); i++) {
            detail.get(i).getProduct().setQuantity(detail.get(i).getProduct().getQuantity() + 1);
        }
        orderDetailService.saveAll(detail);
        
        User user = userService.findID(request.getRemoteUser());
        String email = user.getEmail();
        Order order = orderservice.findId(id);
        Long idorder = order.getId();
        if(user.getEmail() != null) {
            sendEmailCancle(email, idorder);
        }
        return "forward:/order/list";
    }
    @RequestMapping("/cart/view")
    public String view(Model model) {
       
        session.setAttribute("giamgia", 0);
        return "cart/view";
    }
}
