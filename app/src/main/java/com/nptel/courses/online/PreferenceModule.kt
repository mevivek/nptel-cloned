package com.nptel.courses.online

import android.content.Context
import com.nptel.courses.online.entities.Course
import com.nptel.courses.online.utility.getSelectedCourse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class PreferenceModule {

    @Provides
    fun provideSelectedCourse(@ApplicationContext context: Context): Course =
        getSelectedCourse(context = context)!!
}