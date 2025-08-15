package org.moto.motravel.model;

import java.io.Serializable;
import java.util.Objects;

public class HiddenGemBookmarkId implements Serializable {
    private Long userId;
    private Long hiddenGemId;

    // Constructors
    public HiddenGemBookmarkId() {}

    public HiddenGemBookmarkId(Long userId, Long hiddenGemId) {
        this.userId = userId;
        this.hiddenGemId = hiddenGemId;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getHiddenGemId() {
        return hiddenGemId;
    }

    public void setHiddenGemId(Long hiddenGemId) {
        this.hiddenGemId = hiddenGemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HiddenGemBookmarkId)) return false;
        HiddenGemBookmarkId that = (HiddenGemBookmarkId) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(hiddenGemId, that.hiddenGemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, hiddenGemId);
    }
}
