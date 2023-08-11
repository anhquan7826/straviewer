package com.vnpttech.straviewer.data.auth

enum class StravaScope {
    READ {
        override fun toString(): String {
            return "read"
        }
    },
    READ_ALL {
        override fun toString(): String {
            return "read_all"
        }
    },
    PROFILE_READ_ALL {
        override fun toString(): String {
            return "profile:read_all"
        }
    },
    PROFILE_WRITE {
        override fun toString(): String {
            return "profile:write"
        }
    },
    ACTIVITY_READ {
        override fun toString(): String {
            return "activity:read"
        }
    },
    ACTIVITY_READ_ALL {
        override fun toString(): String {
            return "activity:read_all"
        }
    },
    ACTIVITY_WRITE {
        override fun toString(): String {
            return "activity:write"
        }
    }
}