import Dropdown from "../inputs/Dropdown";
import { useRouter } from "../../context/router";
import { FC, ReactNode, useCallback, useEffect, useMemo } from "react";
import styled from "styled-components";
import { DimensionConfig, Named } from "schema/generated/types";
import ActionBar from "./ActionBar";
import HierarchicalBlocks from "./HierachicalBlocks";
import UnwrappedBlocks from "./UnwrappedBlocks";
import {
  useNavigateToPath,
  useProviderPath,
  useShownFilter,
} from "../../util/findRecursive";
import Breadcrumbs from "../Breadcrumbs";

export enum View {
  HIERARCHICAL = "hierarchical",
  UNWRAPPED = "unwrapped",
}

const ConfigVisualizer: FC<{
  config: DimensionConfig;
  options?: Named[];
  children?: ReactNode;
}> = ({ config, options, children, ...props }) => {
  const { query, replace, push } = useRouter();

  const view = useMemo(
    () => (query.view as View) ?? View.HIERARCHICAL,
    [query]
  );

  const setView = useCallback(
    (v: View) => replace({ query: { ...query, view: v } }),
    [query, replace]
  );

  useEffect(() => {
    if (!Object.values(View).includes(view)) {
      setView(View.HIERARCHICAL);
    }
  }, [setView, view]);

  const labelledOptions = useMemo(
    () =>
      options?.map((value) => ({ value, label: `${value.mod}:${value.id}` })),
    [options]
  );

  const path = useProviderPath();
  const shown = useShownFilter(config.blocks, path);
  const navigate = useNavigateToPath();

  return (
    <Style {...props}>
      <ActionBar view={view} onView={setView}>
        {children}
        {labelledOptions && (
          <Dropdown
            id="skygrid-config"
            label=""
            options={labelledOptions}
            onChange={(it) =>
              push({
                query: {
                  ...query,
                  namespace: it?.value.mod,
                  config: it?.value.id,
                },
              })
            }
            value={labelledOptions.find(
              (it) =>
                it.value.mod === query.namespace && it.value.id == query.config
            )}
          />
        )}
      </ActionBar>
      {path && (
        <Breadcrumbs
          root={query.config as string}
          crumbs={path}
          onClick={navigate}
        />
      )}
      {view === View.HIERARCHICAL && <HierarchicalBlocks blocks={shown} />}
      {view === View.UNWRAPPED && <UnwrappedBlocks blocks={shown} />}
    </Style>
  );
};

const Style = styled.div``;

export default ConfigVisualizer;
