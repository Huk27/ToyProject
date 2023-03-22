package com.Jinhyy.search.type;

import com.Jinhyy.constants.Constants;

public enum SortType {
    ACCURACY(0), DATE_DESC(1);

    public final int value;

    SortType(int value) {
        this.value = value;
    }

    public static String convertToStringBySearchDomainType(SearchDomainType searchDomainType, SortType sortType) {
        if (SearchDomainType.KAKAO.equals(searchDomainType)) {
            return sortType.value ==  ACCURACY.value ? Constants.SORT.KAKAO_ACCURATE_SORT : Constants.SORT.KAKAO_RECENT_SORT;
        }
        return sortType.value ==  ACCURACY.value ? Constants.SORT.NAVER_ACCURATE_SORT : Constants.SORT.NAVER_RECENT_SORT;
    }
}
