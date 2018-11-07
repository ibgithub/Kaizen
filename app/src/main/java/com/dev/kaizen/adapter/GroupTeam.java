package com.dev.kaizen.adapter;

import android.os.Parcel;
import android.os.Parcelable;

public class GroupTeam implements Parcelable {
    private int id;
    private String desc, mentorName;

    public GroupTeam() {

    }

    public GroupTeam(int id, String desc, String mentorName) {
        this.id = id;
        this.desc = desc;
        this.mentorName = mentorName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        dest.writeInt(this.id);
        dest.writeString(this.desc);
        dest.writeString(this.mentorName);
    }

    protected GroupTeam(Parcel in) {
        this.id = in.readInt();
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
