package com.wanted.only_one.member.dto;

public class BlacklistDTO {

    private long list_id;
    private long member_id;

    public BlacklistDTO() {}

    public BlacklistDTO(long member_id, long list_id) {
        this.member_id = member_id;
        this.list_id = list_id;
    }

    public long getList_id() {
        return list_id;
    }

    public void setList_id(long list_id) {
        this.list_id = list_id;
    }

    public long getMember_id() {
        return member_id;
    }

    public void setMember_id(long member_id) {
        this.member_id = member_id;
    }

    @Override
    public String toString() {
        return "BlacklistDTO{" +
                "list_id=" + list_id +
                ", member_id=" + member_id +
                '}';
    }
}

