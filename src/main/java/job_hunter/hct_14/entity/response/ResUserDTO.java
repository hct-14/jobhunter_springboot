package job_hunter.hct_14.entity.response;

import job_hunter.hct_14.util.constant.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {
        private int id;
        private String name;
        private String email;
        private GenderEnum gender;
        private String address;
        private int age;
        private Instant createdAt;
        private Instant updatedAt;

        private Role role;
        private CompanyUser company;


        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class CompanyUser{
                private int id;
                private String name;
        }

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Role{
                private int id;
                private String name;
        }
    }


