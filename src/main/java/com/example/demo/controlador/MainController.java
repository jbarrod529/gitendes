package com.example.demo.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entidad.Comentario;
import com.example.demo.servicio.comentario.ComentarioServicio;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
@Controller
public class MainController {
	
	 @Autowired
	 private ComentarioServicio comentarioServicio;

	 private final int TAM_PAGINACION = 5;
	
/**
 * 
 * @return returns index web
 */
    @GetMapping("/")
    public String index() {
        return "index"; // Muestra la página de inicio (index.html)
    }

    /**
     * 
     * 
     *  
     * @return returns login web
     */
    @GetMapping("/login")
    public String showLoginForm(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("SPRING_SECURITY_CONTEXT") != null);
        model.addAttribute("isLoggedIn", isLoggedIn);
        return "public/login"; // Nombre de tu plantilla de inicio de sesión
    }
    

    /**
     * 
     * @param model
     * @param page
     * @param request
     * @return
     */
@GetMapping("/home")
public String home(Model model,  @RequestParam(value = "page", defaultValue = "0") int page,
         HttpServletRequest request ) {

    Slice<Comentario> sliceComentarios = comentarioServicio.listarTodosComoSlice(PageRequest.of(page, TAM_PAGINACION));
    
    int currentPage = page;
    int startPage = Math.max(0, currentPage - 2);
    int endPage = currentPage + 2; // Asume un rango fijo de páginas alrededor de la página actual

    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    model.addAttribute("comentarios", sliceComentarios.getContent());
    model.addAttribute("requestURI", request.getRequestURI());
    model.addAttribute("hasNext", sliceComentarios.hasNext());
    model.addAttribute("hasPrevious", sliceComentarios.hasPrevious());
    model.addAttribute("currentPage", currentPage);


    return "public/home";


}
}