package com.example.demo;

import church.shifu.archive.berylapi.domain.membership.model.MembershipUser;
import church.shifu.archive.berylapi.infrastructure.util.RowVersion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
abstract public class UpdatableEntityBase {

    protected LocalDateTime createTime;

    protected MembershipUser createBy;

    protected LocalDateTime updateTime;

    protected MembershipUser updateBy;

    protected RowVersion rowVersion;
}
