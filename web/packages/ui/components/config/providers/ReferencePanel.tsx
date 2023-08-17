import { Reference } from "schema/generated/types";
import { createElement, FC } from "react";
import { panelComponent } from "../ProviderPanel";

const ReferencePanel: FC<Reference & { size: number }> = ({
  provider,
  ...props
}) => {
  if (!provider) return <p>Invalid Reference</p>;
  return createElement(panelComponent(provider), {
    ...props,
    ...provider,
    name: provider.name ?? props.id,
  });
};

export default ReferencePanel;
