/* eslint-disable */
import { TypedDocumentNode as DocumentNode } from '@graphql-typed-document-node/core';
export type Maybe<T> = T | null;
export type InputMaybe<T> = Maybe<T>;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
export type MakeEmpty<T extends { [key: string]: unknown }, K extends keyof T> = { [_ in K]?: never };
export type Incremental<T> = T | { [P in keyof T]?: P extends ' $fragmentName' | '__typename' ? T[P] : never };
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: { input: string; output: string; }
  String: { input: string; output: string; }
  Boolean: { input: boolean; output: boolean; }
  Int: { input: number; output: number; }
  Float: { input: number; output: number; }
};

export type BaseBlockProvider = {
  except?: Maybe<Array<Filter>>;
  extra?: Maybe<Scalars['Boolean']['output']>;
  extras?: Maybe<Array<Extra>>;
  icon?: Maybe<Scalars['String']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  weight: Scalars['Float']['output'];
};

export type BaseExtra = {
  children: Array<BlockProvider>;
  probability: Scalars['Float']['output'];
};

export type Block = BaseBlockProvider & Named & WeightedEntry & {
  __typename?: 'Block';
  except?: Maybe<Array<Filter>>;
  extra?: Maybe<Scalars['Boolean']['output']>;
  extras?: Maybe<Array<Extra>>;
  icon?: Maybe<Scalars['String']['output']>;
  id: Scalars['String']['output'];
  mod?: Maybe<Scalars['String']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  weight: Scalars['Float']['output'];
};

export type BlockList = BaseBlockProvider & WeightedEntry & {
  __typename?: 'BlockList';
  children: Array<BlockProvider>;
  except?: Maybe<Array<Filter>>;
  extra?: Maybe<Scalars['Boolean']['output']>;
  extras?: Maybe<Array<Extra>>;
  icon?: Maybe<Scalars['String']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  weight: Scalars['Float']['output'];
};

export type BlockProvider = Block | BlockList | Fallback | Reference | Tag;

export type CardinalExtra = BaseExtra & {
  __typename?: 'CardinalExtra';
  children: Array<BlockProvider>;
  offset: Scalars['Int']['output'];
  probability: Scalars['Float']['output'];
};

export type DimensionConfig = {
  __typename?: 'DimensionConfig';
  blocks: Array<BlockProvider>;
  id: Scalars['String']['output'];
  loot: Array<LootTable>;
};

export enum Direction {
  Down = 'DOWN',
  East = 'EAST',
  North = 'NORTH',
  South = 'SOUTH',
  Up = 'UP',
  West = 'WEST'
}

export type Extra = CardinalExtra | OffsetExtra | SideExtra;

export type Fallback = BaseBlockProvider & WeightedEntry & {
  __typename?: 'Fallback';
  children: Array<BlockProvider>;
  except?: Maybe<Array<Filter>>;
  extra?: Maybe<Scalars['Boolean']['output']>;
  extras?: Maybe<Array<Extra>>;
  icon?: Maybe<Scalars['String']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  weight: Scalars['Float']['output'];
};

export type Filter = ModFilter | NameFilter | TagFilter;

export type LootTable = WeightedEntry & {
  __typename?: 'LootTable';
  id?: Maybe<Scalars['String']['output']>;
  weight: Scalars['Float']['output'];
};

export type ModFilter = {
  __typename?: 'ModFilter';
  id: Scalars['String']['output'];
};

export type NameFilter = {
  __typename?: 'NameFilter';
  pattern: Scalars['String']['output'];
};

export type Named = {
  id: Scalars['String']['output'];
  mod?: Maybe<Scalars['String']['output']>;
};

export type OffsetExtra = BaseExtra & {
  __typename?: 'OffsetExtra';
  children: Array<BlockProvider>;
  probability: Scalars['Float']['output'];
  x?: Maybe<Scalars['Int']['output']>;
  y?: Maybe<Scalars['Int']['output']>;
  z?: Maybe<Scalars['Int']['output']>;
};

export type Preset = {
  provider?: Maybe<BlockProvider>;
};

export type Query = {
  __typename?: 'Query';
  config: DimensionConfig;
  configs: Array<DimensionConfig>;
  preset: Preset;
};


export type QueryConfigArgs = {
  id?: InputMaybe<Scalars['String']['input']>;
};


export type QueryPresetArgs = {
  id?: InputMaybe<Scalars['String']['input']>;
};

export type Reference = BaseBlockProvider & WeightedEntry & {
  __typename?: 'Reference';
  except?: Maybe<Array<Filter>>;
  extra?: Maybe<Scalars['Boolean']['output']>;
  extras?: Maybe<Array<Extra>>;
  icon?: Maybe<Scalars['String']['output']>;
  id: Scalars['String']['output'];
  name?: Maybe<Scalars['String']['output']>;
  provider: BlockProvider;
  weight: Scalars['Float']['output'];
};

export type SideExtra = BaseExtra & {
  __typename?: 'SideExtra';
  children: Array<BlockProvider>;
  offset?: Maybe<Scalars['Int']['output']>;
  on?: Maybe<Direction>;
  probability: Scalars['Float']['output'];
};

export type Tag = BaseBlockProvider & Named & WeightedEntry & {
  __typename?: 'Tag';
  except?: Maybe<Array<Filter>>;
  extra?: Maybe<Scalars['Boolean']['output']>;
  extras?: Maybe<Array<Extra>>;
  icon?: Maybe<Scalars['String']['output']>;
  id: Scalars['String']['output'];
  matches: Array<Block>;
  mod?: Maybe<Scalars['String']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  weight: Scalars['Float']['output'];
};

export type TagFilter = Named & {
  __typename?: 'TagFilter';
  id: Scalars['String']['output'];
  mod?: Maybe<Scalars['String']['output']>;
};

export type WeightedEntry = {
  weight: Scalars['Float']['output'];
};

type RawProviderFragment_Block_Fragment = { __typename?: 'Block', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null };

type RawProviderFragment_BlockList_Fragment = { __typename?: 'BlockList', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null };

type RawProviderFragment_Fallback_Fragment = { __typename?: 'Fallback', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null };

type RawProviderFragment_Reference_Fragment = { __typename?: 'Reference', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null };

type RawProviderFragment_Tag_Fragment = { __typename?: 'Tag', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null };

export type RawProviderFragmentFragment = RawProviderFragment_Block_Fragment | RawProviderFragment_BlockList_Fragment | RawProviderFragment_Fallback_Fragment | RawProviderFragment_Reference_Fragment | RawProviderFragment_Tag_Fragment;

type BlockProviderFragment_Block_Fragment = { __typename?: 'Block', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null };

type BlockProviderFragment_BlockList_Fragment = { __typename?: 'BlockList', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, children: Array<{ __typename?: 'Block', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null } | { __typename?: 'BlockList', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Fallback', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Reference', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Tag', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null }> };

type BlockProviderFragment_Fallback_Fragment = { __typename?: 'Fallback', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, children: Array<{ __typename?: 'Block', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null } | { __typename?: 'BlockList', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Fallback', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Reference', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Tag', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null }> };

type BlockProviderFragment_Reference_Fragment = { __typename?: 'Reference', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, provider: { __typename?: 'Block', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null } | { __typename?: 'BlockList', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Fallback', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Reference', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Tag', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null } };

type BlockProviderFragment_Tag_Fragment = { __typename?: 'Tag', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null };

export type BlockProviderFragmentFragment = BlockProviderFragment_Block_Fragment | BlockProviderFragment_BlockList_Fragment | BlockProviderFragment_Fallback_Fragment | BlockProviderFragment_Reference_Fragment | BlockProviderFragment_Tag_Fragment;

export type DimensionConfigFragmentFragment = { __typename?: 'DimensionConfig', blocks: Array<{ __typename?: 'Block', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null } | { __typename?: 'BlockList', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, children: Array<{ __typename?: 'Block', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null } | { __typename?: 'BlockList', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Fallback', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Reference', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Tag', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null }> } | { __typename?: 'Fallback', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, children: Array<{ __typename?: 'Block', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null } | { __typename?: 'BlockList', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Fallback', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Reference', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Tag', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null }> } | { __typename?: 'Reference', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, provider: { __typename?: 'Block', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null } | { __typename?: 'BlockList', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Fallback', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Reference', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Tag', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null } } | { __typename?: 'Tag', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null }>, loot: Array<{ __typename?: 'LootTable', weight: number, id?: string | null }> };

export type GetConfigsQueryVariables = Exact<{ [key: string]: never; }>;


export type GetConfigsQuery = { __typename?: 'Query', configs: Array<{ __typename?: 'DimensionConfig', id: string }> };

export type GetConfigQueryVariables = Exact<{
  id: Scalars['String']['input'];
}>;


export type GetConfigQuery = { __typename?: 'Query', config: { __typename?: 'DimensionConfig', blocks: Array<{ __typename?: 'Block', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null } | { __typename?: 'BlockList', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, children: Array<{ __typename?: 'Block', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null } | { __typename?: 'BlockList', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Fallback', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Reference', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Tag', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null }> } | { __typename?: 'Fallback', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, children: Array<{ __typename?: 'Block', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null } | { __typename?: 'BlockList', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Fallback', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Reference', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Tag', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null }> } | { __typename?: 'Reference', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, provider: { __typename?: 'Block', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null } | { __typename?: 'BlockList', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Fallback', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Reference', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null } | { __typename?: 'Tag', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null } } | { __typename?: 'Tag', name?: string | null, icon?: string | null, weight: number, extra?: boolean | null, id: string, mod?: string | null }>, loot: Array<{ __typename?: 'LootTable', weight: number, id?: string | null }> } };

export const RawProviderFragmentFragmentDoc = {"kind":"Document","definitions":[{"kind":"FragmentDefinition","name":{"kind":"Name","value":"RawProviderFragment"},"typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"BlockProvider"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"BaseBlockProvider"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"name"}},{"kind":"Field","name":{"kind":"Name","value":"icon"}},{"kind":"Field","name":{"kind":"Name","value":"weight"}},{"kind":"Field","name":{"kind":"Name","value":"extra"}}]}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"Named"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"mod"}}]}}]}}]} as unknown as DocumentNode<RawProviderFragmentFragment, unknown>;
export const BlockProviderFragmentFragmentDoc = {"kind":"Document","definitions":[{"kind":"FragmentDefinition","name":{"kind":"Name","value":"BlockProviderFragment"},"typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"BlockProvider"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"FragmentSpread","name":{"kind":"Name","value":"RawProviderFragment"}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"BlockList"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"children"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"FragmentSpread","name":{"kind":"Name","value":"RawProviderFragment"}}]}}]}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"Fallback"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"children"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"FragmentSpread","name":{"kind":"Name","value":"RawProviderFragment"}}]}}]}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"Reference"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"provider"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"FragmentSpread","name":{"kind":"Name","value":"RawProviderFragment"}}]}}]}}]}},{"kind":"FragmentDefinition","name":{"kind":"Name","value":"RawProviderFragment"},"typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"BlockProvider"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"BaseBlockProvider"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"name"}},{"kind":"Field","name":{"kind":"Name","value":"icon"}},{"kind":"Field","name":{"kind":"Name","value":"weight"}},{"kind":"Field","name":{"kind":"Name","value":"extra"}}]}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"Named"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"mod"}}]}}]}}]} as unknown as DocumentNode<BlockProviderFragmentFragment, unknown>;
export const DimensionConfigFragmentFragmentDoc = {"kind":"Document","definitions":[{"kind":"FragmentDefinition","name":{"kind":"Name","value":"DimensionConfigFragment"},"typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"DimensionConfig"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"blocks"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"FragmentSpread","name":{"kind":"Name","value":"BlockProviderFragment"}}]}},{"kind":"Field","name":{"kind":"Name","value":"loot"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"weight"}},{"kind":"Field","name":{"kind":"Name","value":"id"}}]}}]}},{"kind":"FragmentDefinition","name":{"kind":"Name","value":"RawProviderFragment"},"typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"BlockProvider"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"BaseBlockProvider"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"name"}},{"kind":"Field","name":{"kind":"Name","value":"icon"}},{"kind":"Field","name":{"kind":"Name","value":"weight"}},{"kind":"Field","name":{"kind":"Name","value":"extra"}}]}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"Named"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"mod"}}]}}]}},{"kind":"FragmentDefinition","name":{"kind":"Name","value":"BlockProviderFragment"},"typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"BlockProvider"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"FragmentSpread","name":{"kind":"Name","value":"RawProviderFragment"}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"BlockList"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"children"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"FragmentSpread","name":{"kind":"Name","value":"RawProviderFragment"}}]}}]}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"Fallback"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"children"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"FragmentSpread","name":{"kind":"Name","value":"RawProviderFragment"}}]}}]}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"Reference"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"provider"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"FragmentSpread","name":{"kind":"Name","value":"RawProviderFragment"}}]}}]}}]}}]} as unknown as DocumentNode<DimensionConfigFragmentFragment, unknown>;
export const GetConfigsDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"query","name":{"kind":"Name","value":"getConfigs"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"configs"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}}]}}]}}]} as unknown as DocumentNode<GetConfigsQuery, GetConfigsQueryVariables>;
export const GetConfigDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"query","name":{"kind":"Name","value":"getConfig"},"variableDefinitions":[{"kind":"VariableDefinition","variable":{"kind":"Variable","name":{"kind":"Name","value":"id"}},"type":{"kind":"NonNullType","type":{"kind":"NamedType","name":{"kind":"Name","value":"String"}}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"config"},"arguments":[{"kind":"Argument","name":{"kind":"Name","value":"id"},"value":{"kind":"Variable","name":{"kind":"Name","value":"id"}}}],"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"FragmentSpread","name":{"kind":"Name","value":"DimensionConfigFragment"}}]}}]}},{"kind":"FragmentDefinition","name":{"kind":"Name","value":"RawProviderFragment"},"typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"BlockProvider"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"BaseBlockProvider"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"name"}},{"kind":"Field","name":{"kind":"Name","value":"icon"}},{"kind":"Field","name":{"kind":"Name","value":"weight"}},{"kind":"Field","name":{"kind":"Name","value":"extra"}}]}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"Named"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}},{"kind":"Field","name":{"kind":"Name","value":"mod"}}]}}]}},{"kind":"FragmentDefinition","name":{"kind":"Name","value":"BlockProviderFragment"},"typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"BlockProvider"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"FragmentSpread","name":{"kind":"Name","value":"RawProviderFragment"}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"BlockList"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"children"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"FragmentSpread","name":{"kind":"Name","value":"RawProviderFragment"}}]}}]}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"Fallback"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"children"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"FragmentSpread","name":{"kind":"Name","value":"RawProviderFragment"}}]}}]}},{"kind":"InlineFragment","typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"Reference"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"provider"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"FragmentSpread","name":{"kind":"Name","value":"RawProviderFragment"}}]}}]}}]}},{"kind":"FragmentDefinition","name":{"kind":"Name","value":"DimensionConfigFragment"},"typeCondition":{"kind":"NamedType","name":{"kind":"Name","value":"DimensionConfig"}},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"blocks"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"FragmentSpread","name":{"kind":"Name","value":"BlockProviderFragment"}}]}},{"kind":"Field","name":{"kind":"Name","value":"loot"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"weight"}},{"kind":"Field","name":{"kind":"Name","value":"id"}}]}}]}}]} as unknown as DocumentNode<GetConfigQuery, GetConfigQueryVariables>;

      export interface PossibleTypesResultData {
        possibleTypes: {
          [key: string]: string[]
        }
      }
      const result: PossibleTypesResultData = {
  "possibleTypes": {
    "BaseBlockProvider": [
      "Block",
      "BlockList",
      "Fallback",
      "Reference",
      "Tag"
    ],
    "BaseExtra": [
      "CardinalExtra",
      "OffsetExtra",
      "SideExtra"
    ],
    "BlockProvider": [
      "Block",
      "BlockList",
      "Fallback",
      "Reference",
      "Tag"
    ],
    "Extra": [
      "CardinalExtra",
      "OffsetExtra",
      "SideExtra"
    ],
    "Filter": [
      "ModFilter",
      "NameFilter",
      "TagFilter"
    ],
    "Named": [
      "Block",
      "Tag",
      "TagFilter"
    ],
    "Preset": [],
    "WeightedEntry": [
      "Block",
      "BlockList",
      "Fallback",
      "LootTable",
      "Reference",
      "Tag"
    ]
  }
};
      export default result;
    