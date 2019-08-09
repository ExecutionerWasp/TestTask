package com.example.testtask.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class GithubItem implements Serializable {

    private Long id;

    @NonNull
    private String url;
    @NonNull
    private String name;
    @NonNull
    private String img;
}
