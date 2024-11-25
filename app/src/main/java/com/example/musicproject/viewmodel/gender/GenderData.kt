package com.example.musicproject.viewmodel.gender

sealed class GenderData(
    val gender: String,
){
    data object Male: GenderData(
        gender= "Nam",
    )

    data object Female: GenderData(
        gender= "Nữ",
    )

    data object NonBinary: GenderData(
        gender= "Phi nhị giới",
    )

    data object Other: GenderData(
        gender= "Khác",
    )

    data object UnderSpecific: GenderData(
        gender= "Không muốn nêu cụ thể",
    )

}
