/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2025 DBeaver Corp and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jkiss.dbeaver.ext.starrocks.model;

import org.jkiss.code.NotNull;
import org.jkiss.code.Nullable;
import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.ext.generic.model.GenericSchema;
import org.jkiss.dbeaver.ext.generic.model.GenericTableBase;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;
import org.jkiss.dbeaver.model.struct.DBSEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * StarRocks Database - represents a database/schema within a StarRocks catalog.
 */
public class StarRocksDatabase extends GenericSchema {

    public StarRocksDatabase(
        @NotNull StarRocksDataSource dataSource,
        @Nullable StarRocksCatalog catalog,
        @NotNull String schemaName
    ) {
        super(dataSource, catalog, schemaName);
    }

    @NotNull
    @Override
    public StarRocksDataSource getDataSource() {
        return (StarRocksDataSource) super.getDataSource();
    }

    @Nullable
    @Override
    public StarRocksCatalog getCatalog() {
        return (StarRocksCatalog) super.getCatalog();
    }

    @Nullable
    public StarRocksTable getStarRocksTable(@NotNull DBRProgressMonitor monitor, @NotNull String name) throws DBException {
        GenericTableBase table = super.getTable(monitor, name);
        return table instanceof StarRocksTable ? (StarRocksTable) table : null;
    }

    @NotNull
    public List<StarRocksTable> getStarRocksTables(@NotNull DBRProgressMonitor monitor) throws DBException {
        List<StarRocksTable> result = new ArrayList<>();
        for (GenericTableBase table : getPhysicalTables(monitor)) {
            if (table instanceof StarRocksTable) {
                result.add((StarRocksTable) table);
            }
        }
        return result;
    }

    @NotNull
    public List<StarRocksView> getStarRocksViews(@NotNull DBRProgressMonitor monitor) throws DBException {
        List<StarRocksView> result = new ArrayList<>();
        for (GenericTableBase table : getTables(monitor)) {
            if (table instanceof StarRocksView) {
                result.add((StarRocksView) table);
            }
        }
        return result;
    }

    @Nullable
    public StarRocksView getStarRocksView(@NotNull DBRProgressMonitor monitor, @NotNull String name) throws DBException {
        for (StarRocksView view : getStarRocksViews(monitor)) {
            if (view.getName().equals(name)) {
                return view;
            }
        }
        return null;
    }

    @NotNull
    public List<StarRocksMaterializedView> getMaterializedViews(@NotNull DBRProgressMonitor monitor) throws DBException {
        List<StarRocksMaterializedView> result = new ArrayList<>();
        for (GenericTableBase table : getTables(monitor)) {
            if (table instanceof StarRocksMaterializedView) {
                result.add((StarRocksMaterializedView) table);
            }
        }
        return result;
    }

    @Nullable
    public StarRocksMaterializedView getMaterializedView(@NotNull DBRProgressMonitor monitor, @NotNull String name) throws DBException {
        for (StarRocksMaterializedView mv : getMaterializedViews(monitor)) {
            if (mv.getName().equals(name)) {
                return mv;
            }
        }
        return null;
    }

    @NotNull
    @Override
    public Class<? extends DBSEntity> getPrimaryChildType(@Nullable DBRProgressMonitor monitor) throws DBException {
        return StarRocksTableBase.class;
    }
}
