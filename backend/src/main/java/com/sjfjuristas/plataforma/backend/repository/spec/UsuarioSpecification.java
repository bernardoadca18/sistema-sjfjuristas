package com.sjfjuristas.plataforma.backend.repository.spec;

import com.sjfjuristas.plataforma.backend.domain.Usuario;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioSpecification
{
    public static Specification<Usuario> searchUsuario(String busca)
    {
        if (!StringUtils.hasText(busca))
        {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }

        String buscaLower = busca.toLowerCase();

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nomeCompleto")), "%" + buscaLower + "%"));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + buscaLower + "%"));

            predicates.add(criteriaBuilder.like(
                criteriaBuilder.function("REPLACE", String.class, 
                    criteriaBuilder.function("REPLACE", String.class, root.get("cpf"), criteriaBuilder.literal("."), criteriaBuilder.literal("")),
                    criteriaBuilder.literal("-"), criteriaBuilder.literal("")),
                "%" + busca.replaceAll("[.\\-]", "") + "%"
            ));

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}
