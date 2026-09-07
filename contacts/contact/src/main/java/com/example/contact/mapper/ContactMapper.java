package com.example.contact.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.contact.dto.ContactDto;
import com.example.contact.model.Contact;

@Mapper(componentModel = "spring")
public interface ContactMapper {
	
	ContactMapper INSTANCE = Mappers.getMapper(ContactMapper.class);
    
    ContactDto toDto(Contact contact);
    
    Contact toEntity(ContactDto dto);
    
    List<ContactDto> toDtoList(List<Contact> contacts);
    
    List<Contact> toEntityList(List<ContactDto> dtos);
}
