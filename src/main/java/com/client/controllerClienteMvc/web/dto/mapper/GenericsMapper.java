package com.client.controllerClienteMvc.web.dto.mapper;

import com.client.controllerClienteMvc.entity.Usuario;
import com.client.controllerClienteMvc.web.dto.UsuarioResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

public class GenericsMapper {


    public static <S, D> D mapTo(Object source, Class<D> resultClass) {
        return new ModelMapper().map(source, resultClass);
    }

    public static <S, D> List<D> mapList(List<S> sourceList, Class<D> resultClass) {
        return sourceList.stream()
                .map(e -> mapTo(e, resultClass))
                .collect(Collectors.toList());
    }

    // Metodos especificos para resposta UsuarioResponseDto
    public static UsuarioResponseDto toDto(Usuario usuario) {

        String role = usuario.getRole().name().substring("ROLE_".length());
        PropertyMap<Usuario, UsuarioResponseDto> props = new PropertyMap<Usuario, UsuarioResponseDto>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);
        return mapper.map(usuario, UsuarioResponseDto.class);
    }

    public static List<UsuarioResponseDto> mapListResponse(List<Usuario> sourceList) {
        return sourceList.stream()
                .map(GenericsMapper::toDto)
                .collect(Collectors.toList());
    }

}
