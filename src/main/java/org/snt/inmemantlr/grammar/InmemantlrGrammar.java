/**
 * Inmemantlr - In memory compiler for Antlr 4
 * <p>
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 Julian Thome <julian.thome.de@gmail.com>
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **/

package org.snt.inmemantlr.grammar;

import org.antlr.v4.Tool;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.ast.GrammarRootAST;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snt.inmemantlr.tool.MemoryTokenVocabParser;

import java.util.Map;

/**
 * special grammar class that operates in-memory
 */
public class InmemantlrGrammar extends Grammar {

    private static final Logger LOGGER = LoggerFactory.getLogger(InmemantlrGrammar.class);

    private String tokenVocab = "";

    public InmemantlrGrammar(Tool tool, GrammarRootAST ast) {
        super(tool, ast);
    }

    public void setTokenVocab(String tokenVocab) {
        this.tokenVocab = tokenVocab;
    }

    /**
     * We want to touch as little ANTR code as possible. We overload this
     * function to pretend the existence of the token vocab parser
     */
    @Override
    public void importTokensFromTokensFile() {
        if (!tokenVocab.isEmpty()) {
            MemoryTokenVocabParser vparser = new MemoryTokenVocabParser(this, tokenVocab);
            Map<String, Integer> tokens = vparser.load();

            int ret = 0;
            for (String t : tokens.keySet()) {
                if (t.charAt(0) == '\'') {
                    ret = defineStringLiteral(t, tokens.get(t));
                    assert ret != Token.INVALID_TYPE;
                } else {
                    ret = defineTokenName(t, tokens.get(t));
                    assert ret != Token.INVALID_TYPE;
                }
                LOGGER.debug("token " + t + " " + tokens.get(t));

            }
        }
    }
}
