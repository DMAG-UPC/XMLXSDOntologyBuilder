package org.example.XMLParserHelper;

import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;

public record BlameLine(
        int lineNumber,
        String content,
        RevCommit commit,
        PersonIdent author
) {}

