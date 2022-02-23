package com.zzb.AutomaArticle.vo.params;

import lombok.Data;

@Data
public class PageParamsVO {
    private int page = 1;
    private int pageSize = 10;
}
