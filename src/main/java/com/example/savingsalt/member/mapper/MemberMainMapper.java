package com.example.savingsalt.member.mapper;

import com.example.savingsalt.global.EntityMapper;
import com.example.savingsalt.member.domain.dto.MemberDto;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import com.example.savingsalt.member.domain.dto.MemberUpdateResponseDto;
import com.example.savingsalt.member.domain.dto.MyPageResponseDto;
import com.example.savingsalt.member.enums.Gender;
import com.example.savingsalt.member.domain.dto.RepresentativeBadgeSetResDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

public interface MemberMainMapper {

    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface MemberMapper extends EntityMapper<MemberEntity, MemberDto> {
        @Mapping(source = "gender", target = "gender", qualifiedByName = "genderToString")
        MemberDto toDto(MemberEntity entity);

        @Mapping(source = "gender", target = "gender", qualifiedByName = "stringToGender")
        MemberEntity toEntity(MemberDto dto);

        @Named("genderToString")
        static String genderToString(Gender gender) {
            return gender != null ? gender.getKey() : null;
        }

        @Named("stringToGender")
        static Gender stringToGender(String gender) {
            if (gender == null) {
                return null;
            }
            switch (gender) {
                case "GENDER_MALE":
                case "MALE":
                    return Gender.MALE;
                case "GENDER_FEMALE":
                case "FEAMLE":
                    return Gender.FEMALE;
                default:
                    throw new IllegalArgumentException("Unknown gender: " + gender);
            }
        }

        RepresentativeBadgeSetResDto toRepresentativeBadgeSetResDto(MemberEntity member);
    }

    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface MemberUpdateMapper extends EntityMapper<MemberEntity, MemberUpdateResponseDto> {

        @Mapping(source = "gender", target = "gender", qualifiedByName = "genderToString")
        MemberUpdateResponseDto toDto(MemberEntity entity);

        @Mapping(source = "gender", target = "gender", qualifiedByName = "stringToGender")
        MemberEntity toEntity(MemberUpdateResponseDto dto);

        @Named("genderToString")
        static String genderToString(Gender gender) {
            return gender != null ? gender.getKey() : null;
        }

        @Named("stringToGender")
        static Gender stringToGender(String gender) {
            if (gender == null) {
                return null;
            }
            switch (gender) {
                case "GENDER_MALE":
                case "MALE":
                    return Gender.MALE;
                case "GENDER_FEMALE":
                case "FEAMLE":
                    return Gender.FEMALE;
                default:
                    throw new IllegalArgumentException("Unknown gender: " + gender);
            }
        }
    }

    @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
    interface MemberMyPageMapper extends EntityMapper<MemberEntity, MyPageResponseDto> {

        @Mapping(source = "gender", target = "gender", qualifiedByName = "genderToString")
        MyPageResponseDto toDto(MemberEntity entity);

        @Mapping(source = "gender", target = "gender", qualifiedByName = "stringToGender")
        MemberEntity toEntity(MyPageResponseDto dto);

        @Named("genderToString")
        static String genderToString(Gender gender) {
            return gender != null ? gender.getKey() : null;
        }

        @Named("stringToGender")
        static Gender stringToGender(String gender) {
            if (gender == null) {
                return null;
            }
            switch (gender) {
                case "GENDER_MALE":
                case "MALE":
                    return Gender.MALE;
                case "GENDER_FEMALE":
                case "FEAMLE":
                    return Gender.FEMALE;
                default:
                    throw new IllegalArgumentException("Unknown gender: " + gender);
            }
        }
    }
}
