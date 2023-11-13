package org.davydov.model.mapper;

import org.davydov.model.Player;
import org.davydov.model.PlayerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerMapper {
    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    Player toModel(PlayerDTO dto);

    PlayerDTO toDto(Player model);
}
