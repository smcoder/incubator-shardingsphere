/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.core.parse.antlr.extractor.impl.dml.insert;

import com.google.common.base.Optional;
import org.antlr.v4.runtime.ParserRuleContext;
import org.apache.shardingsphere.core.parse.antlr.extractor.api.OptionalSQLSegmentExtractor;
import org.apache.shardingsphere.core.parse.antlr.extractor.impl.common.column.ColumnExtractor;
import org.apache.shardingsphere.core.parse.antlr.extractor.util.ExtractorUtils;
import org.apache.shardingsphere.core.parse.antlr.extractor.util.RuleName;
import org.apache.shardingsphere.core.parse.antlr.sql.segment.dml.column.ColumnSegment;
import org.apache.shardingsphere.core.parse.antlr.sql.segment.dml.column.InsertDuplicateKeyColumnsSegment;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Insert duplicate key columns extractor.
 *
 * @author zhangliang
 */
public final class InsertDuplicateKeyColumnsExtractor implements OptionalSQLSegmentExtractor {
    
    private final ColumnExtractor columnExtractor = new ColumnExtractor();
    
    @Override
    public Optional<InsertDuplicateKeyColumnsSegment> extract(final ParserRuleContext ancestorNode) {
        Optional<ParserRuleContext> onDuplicateKeyClauseNode = ExtractorUtils.findFirstChildNode(ancestorNode, RuleName.INSERT_ON_DUPLICATE_KEY_CLAUSE);
        if (!onDuplicateKeyClauseNode.isPresent()) {
            return Optional.absent();
        }
        return Optional.of(new InsertDuplicateKeyColumnsSegment(extractColumnSegments(onDuplicateKeyClauseNode.get())));
    }
    
    private Collection<ColumnSegment> extractColumnSegments(final ParserRuleContext onDuplicateKeyClauseNode) {
        Collection<ColumnSegment> result = new LinkedList<>();
        for (ParserRuleContext each : ExtractorUtils.getAllDescendantNodes(onDuplicateKeyClauseNode, RuleName.COLUMN_NAME)) {
            Optional<ColumnSegment> columnSegment = columnExtractor.extract(each);
            if (columnSegment.isPresent()) {
                result.add(columnSegment.get());
            }
        }
        return result;
    }
}