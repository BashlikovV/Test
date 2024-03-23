package by.bashlikovvv.core.ext

import by.bashlikovvv.core.domain.model.ParsedException

fun Throwable.toParsedException(
    titleBuilder: (Throwable) -> String
): ParsedException {
    return ParsedException(
        title = titleBuilder(this),
        message = this.message
    )
}