package com.triple.homework.common.exception.user

import com.triple.homework.common.exception.ApplicationException

class UserNotFoundException: ApplicationException(UserErrorCode.NOT_FOUND_USER)