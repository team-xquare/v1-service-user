package com.xquare.v1userservice.domain

import com.xquare.v1userservice.user.User
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Year

internal class UserEntityTest {

    @Test
    fun createEntity() {
        val arr = listOf(1, 3, 5, 7, 8, 10, 20, 35, 99, 100)
        println(arr[binarySearch(20, 0, arr.size - 1, arr)])
    }

    private fun binarySearch(key: Int, low: Int, high: Int, arr: List<Int>): Int {
        var mid: Int
        var highVar = high
        var lowVar = low

        while (lowVar <= highVar) {
            mid = (lowVar + highVar) / 2;

            if (key == arr[mid]) {
                return mid;
            } else if (key < arr[mid]) {
                highVar = mid - 1;
            } else {
                lowVar = mid + 1;
            }
        }
        throw IllegalArgumentException()
    }


    private fun buildCompletedUser() =
        User(
            name = "name",
            password = "testPassword",
            accountId = "accountId",
            birthDay = LocalDate.now(),
            classNum = 2,
            deviceToken = "sdaf",
            entranceYear = Year.now(),
            grade = 1,
            num = 2,
            profileFileName = "sdaf"
        )

}