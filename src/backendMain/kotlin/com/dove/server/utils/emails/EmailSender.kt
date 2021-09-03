package com.dove.server.utils.emails

import com.dove.mailer.Mailer
import com.dove.server.local.Environment.mailer

object EmailSender : Mailer by mailer