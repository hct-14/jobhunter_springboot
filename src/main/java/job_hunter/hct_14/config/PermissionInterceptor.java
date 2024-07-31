package job_hunter.hct_14.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import job_hunter.hct_14.entity.Permission;
import job_hunter.hct_14.entity.Role;
import job_hunter.hct_14.entity.User;
import job_hunter.hct_14.service.RoleService;
import job_hunter.hct_14.service.UserService;
import job_hunter.hct_14.util.SercuryUtil;
import job_hunter.hct_14.util.error.IdInvaldException;
import job_hunter.hct_14.util.error.PermissionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.security.Security;
import java.util.List;

@Transactional
public class PermissionInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {

        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        // check permission
        String email = SercuryUtil.getCurrentUserLogin().isPresent() == true
                ? SercuryUtil.getCurrentUserLogin().get()
                : "";
        if (email != null && !email.isEmpty()) {
            User user = this.userService.handleGetUserByUsername(email);
            if (user != null) {
                Role role = user.getRole();
                if (role != null) {
                    List<Permission> permissions = role.getPermissions();
                    boolean isAllow = permissions.stream().anyMatch(item -> item.getApiPath().equals(path)
                            && item.getMethod().equals(httpMethod));

//                    if (isAllow == false) {
//                        throw new PermissionException("Bạn không có quyền truy cập endpoint này.");
//                    }
                } else {
                    throw new PermissionException("Bạn không có quyền truy cập endpoint này.");
                }
            }
        }

        return true;
    }
}
