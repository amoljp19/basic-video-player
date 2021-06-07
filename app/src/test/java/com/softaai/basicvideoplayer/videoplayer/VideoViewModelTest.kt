package com.softaai.basicvideoplayer.videoplayer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.softaai.basicvideoplayer.mock
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class VideoViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: VideoViewModel

    private val booleanObserver: Observer<Boolean> = mock()

    private val stringObserver: Observer<String> = mock()


    @Before
    fun before() {
        viewModel = VideoViewModel()
    }


    @Test
    fun testPlayButtonEnabled_ifTrueValue() {


        viewModel.isPlayButtonEnabled.value = true

        viewModel.isPlayButtonEnabled.observeForever(booleanObserver)

        val captor = ArgumentCaptor.forClass(Boolean::class.java)
        captor.run {
            verify(booleanObserver, times(1)).onChanged(capture())
            assertEquals(true, value)
        }
    }


    @Test
    fun testPlayButtonNotEnabled_ifFalseValue() {


        viewModel.isPlayButtonEnabled.value = false

        viewModel.isPlayButtonEnabled.observeForever(booleanObserver)

        val captor = ArgumentCaptor.forClass(Boolean::class.java)
        captor.run {
            verify(booleanObserver, times(1)).onChanged(capture())
            assertEquals(false, value)
        }
    }

    @Test
    fun testProgressBarVisible_ifTrueValue() {

        viewModel.isProgressBarVisible.value = true

        viewModel.isProgressBarVisible.observeForever(booleanObserver)

        val captor = ArgumentCaptor.forClass(Boolean::class.java)
        captor.run {
            verify(booleanObserver, times(1)).onChanged(capture())
            assertEquals(true, value)
        }
    }


    @Test
    fun testProgressBarNotVisible_ifFalseValue() {

        viewModel.isProgressBarVisible.value = false

        viewModel.isProgressBarVisible.observeForever(booleanObserver)

        val captor = ArgumentCaptor.forClass(Boolean::class.java)
        captor.run {
            verify(booleanObserver, times(1)).onChanged(capture())
            assertEquals(false, value)
        }
    }


    @Test
    fun testUriStringLiveData_onCorrectFilePath() {
        viewModel.uriString.value = "video file path"

        viewModel.uriString.observeForever(stringObserver)

        val captor = ArgumentCaptor.forClass(String::class.java)
        captor.run {
            verify(stringObserver, times(1)).onChanged(capture())
            assertEquals("video file path", value)
        }
    }


    @Test
    fun testTextProgressLiveData_initialValue() {

        viewModel.uriString.value = "0:0"

        viewModel.uriString.observeForever(stringObserver)

        val captor = ArgumentCaptor.forClass(String::class.java)
        captor.run {
            verify(stringObserver, times(1)).onChanged(capture())
            assertEquals("0:0", value)
        }
    }


    @Test
    fun testTextTotalTimeLiveData_onTotalValue() {
        viewModel.uriString.value = "05:02"

        viewModel.uriString.observeForever(stringObserver)

        val captor = ArgumentCaptor.forClass(String::class.java)
        captor.run {
            verify(stringObserver, times(1)).onChanged(capture())
            assertEquals("05:02", value)
        }
    }

}
