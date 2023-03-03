package ru.practicum.shareit.item.model.comment;

import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.model.item.Item;
import ru.practicum.shareit.user.model.User;

public class CommentMapper {
    public static Comment fromCommentDto(final CommentDto itemDto, final User user, final Item item) {
        return Comment.builder()
                .text(itemDto.getText())
                .item(item)
                .author(user)
                .authorName(user.getName())
                .build();
    }

    public static CommentDto toCommentDto(final Comment item) {
        return CommentDto.builder()
                .id(item.getId())
                .text(item.getText())
                .itemId(item.getItem().getId())
                .authorId(item.getAuthor().getId())
                .authorName(item.getAuthorName())
                .build();
    }

    private CommentMapper() {
    }
}
