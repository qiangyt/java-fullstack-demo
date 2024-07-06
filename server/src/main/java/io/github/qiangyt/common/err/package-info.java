package io.github.qiangyt.common.err;

/**
 * Exception classification system.
 *
 * Follows these principles:
 *
 * 1. Borrow from the mature HTTP status error classification, dividing into categories such as BadRequest (HTTP 4xx)
 * and InternalError (HTTP 5xx). 2. Detailed error codes are represented by enums, with each module/service having its
 * own error code enumeration class. Cross-module/service error code duplication should be minimized but is not strictly
 * enforced. 3. Transparently pass errors across modules/services. 4. Extensive use of custom subclasses is not
 * encouraged. 5. The server side does not handle i18n for error messages; i18n is handled by the frontend, which
 * translates the error key into the appropriate user language.
 *
 * TODO: Currently incomplete, additional predefined subclasses need to be added.
 *
 */
