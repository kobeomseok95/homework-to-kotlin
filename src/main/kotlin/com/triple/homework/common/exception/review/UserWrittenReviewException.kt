package com.triple.homework.common.exception.review

import com.triple.homework.common.exception.ApplicationException

class UserWrittenReviewException: ApplicationException(ReviewErrorCode.WRITTEN_REVIEW)
