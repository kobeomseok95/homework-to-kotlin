package com.triple.homework.common.exception.review

import com.triple.homework.common.exception.ApplicationException

class ReviewNotFoundException: ApplicationException(ReviewErrorCode.REVIEW_NOT_FOUND)
