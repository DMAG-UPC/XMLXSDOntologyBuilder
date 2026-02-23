package org.example.XMLParserHelper;

import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.lib.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitBlameService {

    private final Repository repository;
    private final Map<String, BlameResult> blameCache = new HashMap<>();

    public GitBlameService(Repository repository) {
        this.repository = repository;
    }

    public List<BlameLine> blameRange(
            String filePath,
            int startLine,
            int endLine
    ) throws Exception {

        BlameResult result = blameCache.get(filePath);
        if (result == null) {
            BlameCommand blame = new BlameCommand(repository);
            blame.setFilePath(filePath);
            result = blame.call();
            if (result != null) {
                blameCache.put(filePath, result);
            }
        }

        if (result == null) {
            return List.of();
        }

        List<BlameLine> lines = new ArrayList<>();
        for (int i = startLine - 1; i < endLine; i++) {
            lines.add(new BlameLine(
                    i + 1,
                    result.getResultContents().getString(i),
                    result.getSourceCommit(i),
                    result.getSourceAuthor(i)
            ));
        }
        return lines;
    }
}
