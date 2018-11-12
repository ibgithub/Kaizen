package com.dev.kaizen.adapter;

import android.os.Parcel;
import android.os.Parcelable;

public class GroupTeam implements Parcelable {
    private Long id, member1, member2, member3, member4, member5, member6, member7, member8, member9, member10;

    private String desc, mentorName;

    public GroupTeam() {

    }

    public GroupTeam(Long id, String desc) {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMember1() {
        return member1;
    }

    public void setMember1(Long member1) {
        this.member1 = member1;
    }

    public Long getMember2() {
        return member2;
    }

    public void setMember2(Long member2) {
        this.member2 = member2;
    }

    public Long getMember3() {
        return member3;
    }

    public void setMember3(Long member3) {
        this.member3 = member3;
    }

    public Long getMember4() {
        return member4;
    }

    public void setMember4(Long member4) {
        this.member4 = member4;
    }

    public Long getMember5() {
        return member5;
    }

    public void setMember5(Long member5) {
        this.member5 = member5;
    }

    public Long getMember6() {
        return member6;
    }

    public void setMember6(Long member6) {
        this.member6 = member6;
    }

    public Long getMember7() {
        return member7;
    }

    public void setMember7(Long member7) {
        this.member7 = member7;
    }

    public Long getMember8() {
        return member8;
    }

    public void setMember8(Long member8) {
        this.member8 = member8;
    }

    public Long getMember9() {
        return member9;
    }

    public void setMember9(Long member9) {
        this.member9 = member9;
    }

    public Long getMember10() {
        return member10;
    }

    public void setMember10(Long member10) {
        this.member10 = member10;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

//    int id, String programName, String background, String totalDays, String totalBudget,
//    String problemList, String taskList, String resultList, String urlPhotoBefore,
//    String urlPhotoAfter, String urlVideo, String notes, String participantGroup

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.member1);
        dest.writeLong(this.member2);
        dest.writeLong(this.member3);
        dest.writeLong(this.member4);
        dest.writeLong(this.member5);
        dest.writeLong(this.member6);
        dest.writeLong(this.member7);
        dest.writeLong(this.member8);
        dest.writeLong(this.member9);
        dest.writeLong(this.member10);
        dest.writeString(this.desc);
        dest.writeString(this.mentorName);
    }

    protected GroupTeam(Parcel in) {
        this.id = in.readLong();
        this.member1 = in.readLong();
        this.member2 = in.readLong();
        this.member3 = in.readLong();
        this.member4 = in.readLong();
        this.member5 = in.readLong();
        this.member6 = in.readLong();
        this.member7 = in.readLong();
        this.member8 = in.readLong();
        this.member9 = in.readLong();
        this.member10 = in.readLong();
        this.desc = in.readString();
        this.mentorName = in.readString();
    }

    public static final Parcelable.Creator<GroupTeam> CREATOR = new Parcelable.Creator<GroupTeam>() {
        @Override
        public GroupTeam createFromParcel(Parcel source) {
            return new GroupTeam(source);
        }

        @Override
        public GroupTeam[] newArray(int size) {
            return new GroupTeam[size];
        }
    };
}
