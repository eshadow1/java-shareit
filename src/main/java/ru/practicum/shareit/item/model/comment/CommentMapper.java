package ru.practicum.shareit.item.model.comment;

import ru.practicum.shareit.item.dto.comment.CommentDto;

public class CommentMapper {
    public static Comment fromCommentDto(final CommentDto itemDto, final int userId, final int itemId) {
        return Comment.builder()
                .text(itemDto.getText())
                .itemId(itemId)
                .authorId(userId)
                .authorName(itemDto.getAuthorName())
                .build();
    }

    public static CommentDto toCommentDto(final Comment item) {
        return CommentDto.builder()
                .id(item.getId())
                .text(item.getText())
                .itemId(item.getItemId())
                .authorId(item.getAuthorId())
                .authorName(item.getAuthorName())
                .build();
    }

    private CommentMapper() {
    }
}
